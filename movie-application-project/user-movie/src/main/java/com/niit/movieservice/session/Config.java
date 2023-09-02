package com.niit.movieservice.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession
public class Config {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory("10.96.0.15", 6379);
    }

}
