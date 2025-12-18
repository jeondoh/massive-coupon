package com.jeondoh.core.common.infrastructure.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQSendHelper {

    private final RabbitTemplate rabbitTemplate;

    // 메세지 전송
    public <T> void sendMessage(String exchange, String routingKey, T message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (AmqpException e) {
            log.error("[RabbitMQ] sendMessage 실패 exchange: {}, routingKey: {}", exchange, routingKey);
            log.error(e.getMessage(), e);
        }
    }
}
