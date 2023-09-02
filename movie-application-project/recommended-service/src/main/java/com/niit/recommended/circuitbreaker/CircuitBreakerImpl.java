package com.niit.recommended.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CircuitBreakerImpl {
    private CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
            .minimumNumberOfCalls(4)
            .slowCallDurationThreshold(Duration.ofMillis(4000L))
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(50.0f)
            .waitDurationInOpenState(Duration.ofSeconds(20L))
            .permittedNumberOfCallsInHalfOpenState(1)
            .build();

    @Bean
    public CircuitBreakerRegistry returnCircuitBreakerRegistry() {
        Map<String, CircuitBreakerConfig> circuitBreakerConfigMap = new HashMap<>();
        circuitBreakerConfigMap.put("WindowOf10", this.config);
        return CircuitBreakerRegistry.of(circuitBreakerConfigMap);
    }

    private TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig
            .custom()
            .timeoutDuration(Duration.ofMillis(15000L))
            .build();

    @Bean
    public TimeLimiterRegistry returnTimeLimiterRegistry() {
        Map<String, TimeLimiterConfig> timeLimiterConfigMap = new HashMap<>();
        timeLimiterConfigMap.put("TimeoutIn15Seconds", timeLimiterConfig);
        return TimeLimiterRegistry.of(timeLimiterConfig);
    }

}
