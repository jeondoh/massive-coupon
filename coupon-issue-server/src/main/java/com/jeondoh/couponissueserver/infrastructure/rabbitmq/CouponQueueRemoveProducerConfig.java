package com.jeondoh.couponissueserver.infrastructure.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponQueueRemoveProducerConfig {

    @Value("${rabbitmq.queues.coupon-queue-remove.exchange-name}")
    private String exchangeName;

    @Bean
    public DirectExchange couponQueueRemoveExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

}
