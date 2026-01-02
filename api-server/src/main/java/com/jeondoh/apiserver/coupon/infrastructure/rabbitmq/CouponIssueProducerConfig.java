package com.jeondoh.apiserver.coupon.infrastructure.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponIssueProducerConfig {

    @Value("${rabbitmq.queues.coupon-issue.exchange-name}")
    private String exchangeName;

    @Bean
    public DirectExchange couponIssueExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

}
