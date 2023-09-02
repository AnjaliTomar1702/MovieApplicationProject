package com.niit.userauthentication.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/login", "/api/v1/save", "/api/v1/image"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider())
                .authorizeHttpRequests(i -> i.anyRequest().permitAll())
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/admin/login"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider())
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/admin/login").hasRole("ADMIN"))
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
        http.securityMatchers(i -> i.requestMatchers("/api/v1/forgotpassword", "/api/v1/resetpassword"))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .httpBasic()
                .disable()
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider())
                .authorizeHttpRequests(i -> i.requestMatchers("/api/v1/forgotpassword", "/api/v1/resetpassword").hasRole("ANONYMOUS"))
                .addFilterAt(new AnonymousFilterForgotPassword(), AnonymousAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 3)
    public SecurityFilterChain filterChain4(HttpSecurity http) throws Exception {
        http.securityMatcher("/v3/api-docs/**", "/webjars/swagger-ui/**", "/**")
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
                .authorizeHttpRequests(i -> i.anyRequest().hasRole("ADMIN"))
                .addFilterBefore(new FilterForToken(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

