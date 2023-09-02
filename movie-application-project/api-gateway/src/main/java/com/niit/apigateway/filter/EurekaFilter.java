package com.niit.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class EurekaFilter implements GatewayFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = null;
        try {
            uri = new URI("http", null, "10.96.0.11", 8761, "/", null, null);
        }
        catch (Exception e){
            System.out.println("Invalid Url schema");
        }
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, uri);
        return chain.filter(exchange);
    }
}
