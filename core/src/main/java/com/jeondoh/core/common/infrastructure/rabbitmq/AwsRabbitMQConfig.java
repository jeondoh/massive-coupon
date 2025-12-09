package com.jeondoh.core.common.infrastructure.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

@EnableRabbit
@Configuration
public class AwsRabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.rabbitmq.cache-size}")
    private int cacheSize;

    @Value("${spring.rabbitmq.template.reply-timeout}")
    private int replyTimeout;

    @Value("${spring.rabbitmq.ssl.enabled:false}")
    private boolean sslEnabled;

    @Bean(name = "rabbitConnectionFactory")
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setConnectionTimeout(connectionTimeout);

        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(cacheSize);

        if (sslEnabled) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore) null);
                sslContext.init(null, tmf.getTrustManagers(), null);

                connectionFactory.getRabbitConnectionFactory().useSslProtocol(sslContext);
            } catch (Exception e) {
                throw new IllegalStateException("RabbitMQ SSL 설정 실패", e);
            }
        }

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        rabbitTemplate.setReplyTimeout(replyTimeout);
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
