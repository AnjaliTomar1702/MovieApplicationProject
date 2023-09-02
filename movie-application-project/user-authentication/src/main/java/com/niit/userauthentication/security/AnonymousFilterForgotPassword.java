package com.niit.userauthentication.security;

import com.niit.userauthentication.exception.InvalidCredentialsException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public class AnonymousFilterForgotPassword extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext sc = SecurityContextHolder.getContext();
        System.err.println("anonymous authentication before:" + sc.getAuthentication());
        if(sc.getAuthentication() != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        List<NameValuePair> params;
        try {
            params = URLEncodedUtils.parse(new URI(httpServletRequest.getRequestURI() + '?' + httpServletRequest.getQueryString()), StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String email = params.stream()
                .filter(i -> i.getName().equalsIgnoreCase("email"))
                .findFirst()
                .orElseThrow(InvalidCredentialsException::new)
                .getValue();
        PreAuthenticatedAuthenticationToken at = new PreAuthenticatedAuthenticationToken(email, "", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        sc.setAuthentication(at);
        HttpSession httpSession = httpServletRequest.getSession(true);
        System.err.println("id:" + httpSession.getId());
        httpSession.getAttributeNames().asIterator().forEachRemaining(i -> System.err.println(httpSession.getAttribute(i)));
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        System.err.println("anonymous authentication after:" + sc.getAuthentication());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
