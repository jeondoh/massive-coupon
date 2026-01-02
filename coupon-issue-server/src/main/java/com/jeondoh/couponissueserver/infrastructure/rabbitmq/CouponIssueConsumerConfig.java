package com.jeondoh.couponissueserver.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jeondoh.core.common.util.StaticVariables.DLQ_EXCHANGE_KEY;
import static com.jeondoh.core.common.util.StaticVariables.DLQ_ROUTING_KEY;

@Configuration
public class CouponIssueConsumerConfig {

    @Value("${rabbitmq.queues.coupon-issue.queue-name}")
    private String queueName;

    @Value("${rabbitmq.queues.coupon-issue.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.queues.coupon-issue.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.queues.coupon-issue.ttl}")
    private int ttl;

    @Value("${rabbitmq.queues.coupon-issue.maxLength}")
    private int maxLength;

    @Value("${rabbitmq.queues.coupon-issue.dlq.name}")
    private String dlqName;

    @Value("${rabbitmq.queues.coupon-issue.dlq.routing-key}")
    private String dlqRoutingKey;

    @Value("${rabbitmq.queues.coupon-issue.dlq.topic-dlx}")
    private String dlqTopicDlx;

    @Bean
    public Queue couponIssueQueue() {
        return QueueBuilder.durable(queueName)
                .ttl(ttl)
                .maxLength(maxLength)
                .withArgument(DLQ_EXCHANGE_KEY, dlqTopicDlx)
                .withArgument(DLQ_ROUTING_KEY, dlqRoutingKey)
                .build();
    }

    @Bean
    public DirectExchange couponIssueExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding couponIssueBinding(Queue couponIssueQueue, DirectExchange couponIssueExchange) {
        return BindingBuilder.bind(couponIssueQueue)
                .to(couponIssueExchange)
                .with(routingKey);
    }

    @Bean
    public Queue couponIssueDLQQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public DirectExchange couponIssueDLQExchange() {
        return new DirectExchange(dlqTopicDlx, true, false);
    }

    @Bean
    public Binding couponIssueDLQBinding(Queue couponIssueDLQQueue, DirectExchange couponIssueDLQExchange) {
        return BindingBuilder.bind(couponIssueDLQQueue)
                .to(couponIssueDLQExchange)
                .with(dlqRoutingKey);
    }

}
