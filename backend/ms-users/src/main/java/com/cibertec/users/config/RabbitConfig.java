package com.cibertec.users.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "logistics.exchange";
    public static final String USERS_EVENTS_QUEUE = "users.shipment.events.queue";

    @Bean
    TopicExchange logisticsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue usersEventsQueue() {
        return new Queue(USERS_EVENTS_QUEUE);
    }

    @Bean
    Binding usersEventsBinding(Queue usersEventsQueue, TopicExchange logisticsExchange) {
        return BindingBuilder.bind(usersEventsQueue).to(logisticsExchange).with("shipment.#");
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
