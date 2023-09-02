package com.niit.recommended;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class RecommendedApplication {
    public static void main( String[] args ) {
        SpringApplication.run(RecommendedApplication.class, args);
    }
}
