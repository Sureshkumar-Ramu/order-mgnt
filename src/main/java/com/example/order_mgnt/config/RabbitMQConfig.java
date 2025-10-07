package com.example.order_mgnt.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String BOOK_UPDATE_QUEUE = "book.update.queue";
    public static final String BOOK_UPDATE_DLQ = "book.update.dlq";
    public static final String BOOK_UPDATE_EXCHANGE = "book.update.exchange";
    public static final String BOOK_UPDATE_ROUTING_KEY = "book.update";

    // DLQ queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(BOOK_UPDATE_DLQ).build();
    }

    // Main queue with DLQ and retry mechanism
    @Bean
    public Queue bookUpdateQueue() {
        return QueueBuilder.durable(BOOK_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", BOOK_UPDATE_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", BOOK_UPDATE_DLQ)
                .withArgument("x-message-ttl", 5000) // 5 seconds retry delay
                .build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(BOOK_UPDATE_EXCHANGE);
    }

    @Bean
    public Binding mainBinding() {
        return BindingBuilder.bind(bookUpdateQueue())
                .to(exchange())
                .with(BOOK_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(exchange())
                .with(BOOK_UPDATE_DLQ);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

