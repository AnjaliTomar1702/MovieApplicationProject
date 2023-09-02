package com.niit.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ResetPasswordFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest()
                .mutate()
                .headers(i -> i.add("Cookie", "SESSION=" + exchange.getRequest().getQueryParams().getFirst("session")))
                .build();
        return chain.filter(exchange);
    }
}
