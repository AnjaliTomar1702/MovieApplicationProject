package com.niit.recommended.security;


import com.niit.recommended.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public class FilterForToken implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String token = httpServletRequest.getHeader("Authorization");
        if(token == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        token = token.substring(7);
        Claims claims = Jwts.parser().setSigningKey("sfrswfes832zd&54cd55ddshfvTrsf$se6d67ds66s66sfd5f5").parseClaimsJws(token).getBody();
        if(claims.getExpiration().before(Date.from(Instant.now())))
            throw new TokenExpiredException();
        Collection<SimpleGrantedAuthority> e = Arrays.stream(claims.get("roles").toString().split(",")).map(SimpleGrantedAuthority::new).toList();
        PreAuthenticatedAuthenticationToken at = new PreAuthenticatedAuthenticationToken(claims.getSubject(), "", e);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(at);
        httpServletRequest.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
