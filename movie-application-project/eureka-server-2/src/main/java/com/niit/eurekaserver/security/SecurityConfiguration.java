package com.niit.eurekaserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.ForceEagerSessionCreationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/eureka", "/eureka**", "/eureka/***", "/", ""))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authorizeHttpRequests(i -> i.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .headers()
                .contentTypeOptions()
                .disable()
                .and()
                .headers()
                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors 'self' http://34.83.1.21 http://localhost:4200"))
                .and()
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider())
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**").hasRole("ADMIN"))
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

