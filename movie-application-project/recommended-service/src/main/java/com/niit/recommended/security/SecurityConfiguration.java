package com.niit.recommended.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/recommended/**"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authorizeHttpRequests(i -> i.anyRequest().permitAll())
                .addFilterBefore(new FilterForToken(), AnonymousAuthenticationFilter.class);
        return http.build();
    }




    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**", "/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .httpBasic()
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
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**", "/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);;
        return http.build();
    }



}

