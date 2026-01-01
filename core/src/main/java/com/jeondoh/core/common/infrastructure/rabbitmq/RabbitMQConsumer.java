package com.jeondoh.core.common.infrastructure.rabbitmq;

import com.jeondoh.core.common.exception.BaseCoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {

    public <T> void consume(T arg, Consumer<T> consumer) {
        try {
            consumer.run(arg);
        } catch (Exception e) {
            log.error("[RabbitMQ] RabbitListener consume 실패 arg: {}", arg);
            log.error(e.getMessage(), e);
            throw BaseCoreException.amqpConsumeException();
        }
    }

    @FunctionalInterface
    public interface Consumer<T> {
        void run(T arg) throws Exception;
    }
}
