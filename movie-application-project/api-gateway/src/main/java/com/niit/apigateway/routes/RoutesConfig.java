package com.niit.apigateway.routes;

import com.niit.apigateway.filter.AdminUrlsFilter;
import com.niit.apigateway.filter.EurekaFilter;
import com.niit.apigateway.filter.ResetPasswordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;

import static org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER;

@Configuration
public class RoutesConfig {

    @Autowired
    private Environment environment;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(i -> i.path("/api/v1/login"
                        , "/api/v1/image"
                        , "/api/v1/admin/login"
                        , "/api/v1/admin/users"
                        , "/api/v1/admin/users/block"
                        , "/api/v1/admin/users/unblock"
                        , "/api/v1/admin/users/addrole"
                        , "/api/v1/admin/users/removerole"
                        , "/api/v1/admin/roles")
                        .filters(j -> j.retry(k -> k.setRetries(2)
                                .setBackoff(new RetryGatewayFilterFactory.BackoffConfig(Duration.ofMillis(5L)
                                        , Duration.ofSeconds(5L)
                                        , 2
                                        , true))))
                        .uri("lb://user-authentication"))
                .route(i -> i.path("/api/v1/forgotpassword")
                        .filters(j -> j.removeResponseHeader("Set-Cookie"))
                        .uri("lb://user-authentication"))
                .route(i -> i.path("/api/v1/resetpassword")
                        .filters(j -> j.filter(new ResetPasswordFilter()))
                        .uri("lb://user-authentication"))
                .route(i -> i.path("/api/v1/register"
                        , "/api/v1/user/**").uri("lb://user-movie"))
                .route(i -> i.path("/api/v1/recommended/**")
                        .filters(j -> j.retry(k -> k.setRetries(2)
                                .setBackoff(new RetryGatewayFilterFactory.BackoffConfig(Duration.ofMillis(5L)
                                        , Duration.ofSeconds(5L)
                                        , 2
                                        , true))))
                        .uri("lb://recommended-service"))
                .route(i -> i.path("/eureka")
                        .filters(j -> j.filter(new EurekaFilter(), ROUTE_TO_URL_FILTER_ORDER + 1)
                                .addResponseHeader("Access-Control-Allow-Credentials", "true")
                                .retry(k -> k.setRetries(2)
                                .setBackoff(new RetryGatewayFilterFactory.BackoffConfig(Duration.ofMillis(5L)
                                        , Duration.ofSeconds(5L)
                                        , 2
                                        , true))))
                        .uri("no://op"))
                .route(i -> i.path("/eureka/**").filters(j -> j.addResponseHeader("Access-Control-Allow-Credentials", "true"))
                        .uri("lb://eureka-server-1"))
                .route(i -> i.path("/api/v1/admin/*/actuator"
                        , "/api/v1/admin/*/actuator**"
                        , "/api/v1/admin/*/actuator/**"
                        , "/api/v1/admin/*/swagger-ui"
                        , "/api/v1/admin/*/swagger-ui**"
                        , "/api/v1/admin/*/swagger-ui/**"
                        , "/api/v1/admin/*/v3/api-docs"
                        , "/api/v1/admin/*/v3/api-docs**"
                        , "/api/v1/admin/*/v3/api-docs/**")
                        .and()
                        .not(j -> j.path("/api/v1/admin/" + environment.getProperty("MY_POD_IP") + "/**"))
                        .filters(j -> j.filter(new AdminUrlsFilter()
                                , ROUTE_TO_URL_FILTER_ORDER + 1).addResponseHeader("Access-Control-Allow-Credentials", "true")
                                .retry(k -> k.setRetries(2)
                                        .setBackoff(new RetryGatewayFilterFactory.BackoffConfig(Duration.ofMillis(5L)
                                                , Duration.ofSeconds(5L)
                                                , 2
                                                , true))))
                        .uri("no://op"))
                .build();
    }
}
