package com.niit.movieservice.rabbit;

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

    private final String notificationExchange = "notification-exchange";

    private final String notificationConsumeQueue = "notification-consume-queue";
    private final String notificationDeliveredQueue = "notification-delivered-queue";

    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(this.notificationExchange, true, false);
        directExchange.setDelayed(true);
        return directExchange;
    }

    @Bean
    public Queue notificationConsumeQueue(){
        return new Queue(this.notificationConsumeQueue, true);
    }

    @Bean
    public Queue notificationDeliveredQueue(){
        return new Queue(this.notificationDeliveredQueue, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Binding bindingNotificationConsume(DirectExchange directExchange, @Qualifier("notificationConsumeQueue") Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(directExchange).with("notification-consume-routing");
    }

    @Bean
    Binding bindingNotificationDelivered(DirectExchange directExchange, @Qualifier("notificationDeliveredQueue") Queue registerQueue){
        return BindingBuilder.bind(registerQueue).to(directExchange).with("notification-delivered-routing");
    }

}
