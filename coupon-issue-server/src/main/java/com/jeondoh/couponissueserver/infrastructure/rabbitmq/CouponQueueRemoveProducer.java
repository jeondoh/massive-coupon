package com.jeondoh.couponissueserver.infrastructure.rabbitmq;

import com.jeondoh.core.servlet.infrastructure.rabbitmq.RabbitMQProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CouponQueueRemoveProducer extends RabbitMQProducer {

    public CouponQueueRemoveProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.queues.coupon-queue-remove.exchange-name}") String exchangeName,
            @Value("${rabbitmq.queues.coupon-queue-remove.routing-key}") String routingKey
    ) {
        super(rabbitTemplate, exchangeName, routingKey);
    }

}
