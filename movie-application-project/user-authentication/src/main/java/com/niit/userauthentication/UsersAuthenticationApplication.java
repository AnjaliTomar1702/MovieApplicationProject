package com.niit.userauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class UsersAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersAuthenticationApplication.class, args);
	}

}
