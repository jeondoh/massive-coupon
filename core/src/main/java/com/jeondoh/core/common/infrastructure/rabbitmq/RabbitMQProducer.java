package com.jeondoh.core.common.infrastructure.rabbitmq;

import com.jeondoh.core.common.exception.BaseCoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public abstract class RabbitMQProducer {

    protected final RabbitTemplate rabbitTemplate;
    protected final String exchangeName;
    protected final String routingKey;

    protected RabbitMQProducer(RabbitTemplate rabbitTemplate, String exchangeName, String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    // 메세지 전송
    public <T> void sendMessage(T message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        } catch (AmqpException e) {
            log.error("[RabbitMQ] sendMessage 실패 exchange: {}, routingKey: {}", exchangeName, routingKey);
            log.error(e.getMessage(), e);
            throw BaseCoreException.amqpProduceException();
        }
    }
}
