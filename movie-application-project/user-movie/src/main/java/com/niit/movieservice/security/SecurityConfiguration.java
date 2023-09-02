package com.niit.movieservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
        http.securityMatchers(i -> i.requestMatchers("/api/v1/register"))
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
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/user/**"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider())
                .authorizeHttpRequests(i -> i.anyRequest().hasAnyRole("USER","ADMIN"))
                .addFilterBefore(new FilterForToken(), AnonymousAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
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
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/admin", "/api/v1/admin**", "/api/v1/admin/**").hasRole("ADMIN"))
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);;
        return http.build();
    }

}

