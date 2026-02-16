package com.cibertec.logistics.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "logistics.exchange";
    public static final String CREATED_KEY = "shipment.created";
    public static final String STATUS_CHANGED_KEY = "shipment.status.changed";

    @Bean
    TopicExchange logisticsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
