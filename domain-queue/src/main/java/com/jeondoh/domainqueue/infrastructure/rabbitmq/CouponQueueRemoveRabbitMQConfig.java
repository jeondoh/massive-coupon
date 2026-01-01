package com.jeondoh.domainqueue.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jeondoh.core.common.util.StaticVariables.DLQ_EXCHANGE_KEY;
import static com.jeondoh.core.common.util.StaticVariables.DLQ_ROUTING_KEY;

@Configuration
public class CouponQueueRemoveRabbitMQConfig {

    @Value("${rabbitmq.queues.coupon-queue-remove.queue-name}")
    private String queueName;

    @Value("${rabbitmq.queues.coupon-queue-remove.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.queues.coupon-queue-remove.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.queues.coupon-queue-remove.prefetch-count}")
    private int prefetchCount;

    @Value("${rabbitmq.queues.coupon-queue-remove.concurrent-consumers}")
    private int concurrentConsumers;

    @Value("${rabbitmq.queues.coupon-queue-remove.max-concurrent-consumers}")
    private int maxConcurrentConsumers;

    @Value("${rabbitmq.queues.coupon-queue-remove.ttl}")
    private int ttl;

    @Value("${rabbitmq.queues.coupon-queue-remove.maxLength}")
    private int maxLength;

    @Value("${rabbitmq.queues.coupon-queue-remove.dlq.name}")
    private String dlqName;

    @Value("${rabbitmq.queues.coupon-queue-remove.dlq.routing-key}")
    private String dlqRoutingKey;

    @Value("${rabbitmq.queues.coupon-queue-remove.dlq.topic-dlx}")
    private String dlqTopicDlx;

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
                .withArgument(DLQ_EXCHANGE_KEY, dlqTopicDlx)
                .withArgument(DLQ_ROUTING_KEY, dlqRoutingKey)
                .build();
    }

    @Bean
    public TopicExchange deadLetterQueueRemoveQueueExchange() {
        return new TopicExchange(dlqTopicDlx);
    }

    @Bean
    public Queue deadLetterQueueRemoveQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding deadLetterQueueRemoveQueueBinding() {
        return BindingBuilder.bind(deadLetterQueueRemoveQueue()).to(deadLetterQueueRemoveQueueExchange()).with(dlqRoutingKey);
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
