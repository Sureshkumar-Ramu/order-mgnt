package com.example.order_mgnt.rabbitmq.publisher;

import com.example.order_mgnt.config.RabbitMQConfig;
import com.example.order_mgnt.dto.common.BookQuantityUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookUpdatePublisher {
    private final RabbitTemplate rabbitTemplate;

    public void sendUpdate(BookQuantityUpdateMessage message) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(
                "book.update.exchange",
                "book.update",
                message
        );
        log.info("Book quantity update message sent: {}", message);
    }
}
