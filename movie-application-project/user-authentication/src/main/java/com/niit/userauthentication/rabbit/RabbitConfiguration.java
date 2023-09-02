package com.niit.userauthentication.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    private final String userEmailExchange = "user-email-exchange";

    private final String userForgotPasswordQueue = "user-forgot-password-queue";
    private final String userVerifyEmailQueue = "user-verify-email-queue";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(this.userEmailExchange, true, false);
    }

    @Bean
    public Queue userForgotPasswordQueue(){
        return new Queue(this.userForgotPasswordQueue, true);
    }

    @Bean
    public Queue userVerifyEmailQueue(){
        return new Queue(this.userVerifyEmailQueue, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Binding bindingUserForgotPassword(DirectExchange directExchange, @Qualifier("userForgotPasswordQueue") Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(directExchange).with("user-forgot-password-routing");
    }

    @Bean
    Binding bindingUserVerifyEmail(DirectExchange directExchange, @Qualifier("userVerifyEmailQueue") Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(directExchange).with("user-verify-email-routing");
    }

}
