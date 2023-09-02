package com.niit.apigateway.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class DummyPreAuthenticatedAuthenticationManager implements ReactiveAuthenticationManager {
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication);
    }
}