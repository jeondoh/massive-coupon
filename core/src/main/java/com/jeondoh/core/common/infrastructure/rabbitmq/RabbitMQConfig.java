package com.jeondoh.core.common.infrastructure.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.dlq.name}")
    private String dlqName;

    @Value("${spring.rabbitmq.dlq.routing-key}")
    private String dlqRoutingKey;

    @Value("${spring.rabbitmq.dlq.topic-dlx}")
    private String dlqTopicDlx;

    @Value("${spring.rabbitmq.dlq.topic-exchange}")
    private String dlqTopicExchange;

    @Value("${spring.rabbitmq.dlq.complete-queue}")
    private String dlqCompleteQueue;

    @Value("${spring.rabbitmq.dlq.complete-routing-key}")
    private String dlqCompleteRoutingKey;

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(dlqTopicExchange);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(dlqTopicDlx);
    }

    @Bean
    public Queue domainQueue() {
        return QueueBuilder.durable(dlqCompleteQueue)
                .withArgument("x-dead-letter-exchange", dlqTopicDlx)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding domainQueueBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(orderExchange()).with(dlqCompleteRoutingKey);
    }

    @Bean
    public Binding deadLetterQueueBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(dlqRoutingKey);
    }

}
