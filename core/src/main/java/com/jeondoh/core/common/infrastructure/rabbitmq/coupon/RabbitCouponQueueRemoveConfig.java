package com.jeondoh.core.common.infrastructure.rabbitmq.coupon;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitCouponQueueRemoveConfig {

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.queue-name}")
    private String queueName;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.exchange-name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.prefetch-count}")
    private int prefetchCount;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.concurrent-consumers}")
    private int concurrentConsumers;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.max-concurrent-consumers}")
    private int maxConcurrentConsumers;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.ttl}")
    private int ttl;

    @Value("${spring.rabbitmq.queues.coupon-queue-remove.maxLength}")
    private int maxLength;

    @Bean
    public SimpleMessageListenerContainer couponQueueRemoveContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setConcurrentConsumers(concurrentConsumers);
        container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        container.setPrefetchCount(prefetchCount);
        return container;
    }

    @Bean
    public Queue couponQueueRemoveQueue() {
        return QueueBuilder.durable(queueName)
                .ttl(ttl)
                .maxLength(maxLength)
                .build();
    }

    @Bean
    public DirectExchange couponQueueRemoveExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding couponQueueRemoveBinding(Queue couponQueueRemoveQueue, DirectExchange couponQueueRemoveExchange) {
        return BindingBuilder.bind(couponQueueRemoveQueue)
                .to(couponQueueRemoveExchange)
                .with(routingKey);
    }

}
