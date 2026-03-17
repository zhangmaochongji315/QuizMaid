package com.kanade.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_IMPORT = "queue.question.import";
    public static final String EXCHANGE_IMPORT = "exchange.question.import";
    public static final String ROUTING_KEY_IMPORT = "routing.key.question.import";

    @Bean
    public Queue importQueue() {
        return QueueBuilder.durable(QUEUE_IMPORT).build();
    }

    @Bean
    public DirectExchange importExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_IMPORT).durable(true).build();
    }

    @Bean
    public Binding importBinding(Queue importQueue, DirectExchange importExchange) {
        return BindingBuilder.bind(importQueue).to(importExchange).with(ROUTING_KEY_IMPORT);
    }
}