package com.niit.pushnotification.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .csrf()
                .disable()
                .cors()
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
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**").permitAll())
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

