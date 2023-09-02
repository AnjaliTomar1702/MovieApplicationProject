package com.niit.apigateway.security;

import com.niit.apigateway.exceptions.InvalidCredentialsException;
import com.niit.apigateway.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain filterChain1(ServerHttpSecurity http) throws Exception {
        http
                //.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .securityMatcher(i -> {
            String path = i.getRequest().getPath().subPath(0).toString();
//            if(path.contains("/api/v1/admin/" + this.environment.getProperty("MY_POD_IP")))
//                return ServerWebExchangeMatcher.MatchResult.match();
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        })
                .authenticationManager(new DummyPreAuthenticatedAuthenticationManager())
                .authorizeExchange(i -> i.anyExchange().hasRole("ADMIN"))
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .addFilterAt(customAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityWebFilterChain filterChain2(ServerHttpSecurity http) throws Exception {
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .securityMatcher(i -> ServerWebExchangeMatcher.MatchResult.match())
                .authorizeExchange(i -> i.pathMatchers("**", "/", "/**").permitAll())
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .headers()
                .disable();
        return http.build();
    }

    private AuthenticationWebFilter customAuthenticationWebFilter(){
        AuthenticationWebFilter authenticationWebFilter;
        authenticationWebFilter = new AuthenticationWebFilter(new DummyPreAuthenticatedAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(i -> {
            String token = i.getRequest().getHeaders().getFirst("Authorization");
            if(token == null) {
                throw new InvalidCredentialsException();
            }
            token = token.substring(7);
            Claims claims = Jwts.parser().setSigningKey("sfrswfes832zd&54cd55ddshfvTrsf$se6d67ds66s66sfd5f5").parseClaimsJws(token).getBody();
            if(claims.getExpiration().before(Date.from(Instant.now())))
                throw new TokenExpiredException();
            Collection<SimpleGrantedAuthority> e = Arrays.stream(claims.get("roles").toString().split(",")).map(SimpleGrantedAuthority::new).toList();
            PreAuthenticatedAuthenticationToken at = new PreAuthenticatedAuthenticationToken(claims.getSubject(), "", e);
            return Mono.just(at);
        });
        return authenticationWebFilter;
    }
}


