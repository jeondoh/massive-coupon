package com.jeondoh.domainqueue.infrastructure.rabbitmq;

import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.core.common.infrastructure.rabbitmq.RabbitMQConsumer;
import com.jeondoh.domainqueue.domain.service.QueueRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponQueueRemoveConsumer {

    private final RabbitMQConsumer rabbitMQConsumer;
    private final QueueRunningService queueRunningService;

    @RabbitListener(queues = "${spring.rabbitmq.queues.coupon-queue-remove.queue-name}")
    public void handleCouponRemove(@Payload CouponIssuedRemoveAtRunningQueueMessage message) {
        rabbitMQConsumer.consume(message, queueRunningService::removeRunningQueueMember);
    }

}
