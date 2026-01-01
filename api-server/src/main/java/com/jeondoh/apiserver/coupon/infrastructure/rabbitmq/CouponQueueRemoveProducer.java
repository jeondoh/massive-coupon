package com.jeondoh.apiserver.coupon.infrastructure.rabbitmq;

import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.core.common.infrastructure.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CouponQueueRemoveProducer {

    public CouponQueueRemoveProducer(
            RabbitMQProducer rabbitmqProducer,
            @Value("${rabbitmq.queues.coupon-queue-remove.exchange-name}") String exchangeName,
            @Value("${rabbitmq.queues.coupon-queue-remove.routing-key}") String routingKey
    ) {
        this.rabbitmqProducer = rabbitmqProducer;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    private final RabbitMQProducer rabbitmqProducer;
    private final String exchangeName;
    private final String routingKey;

    // running queue 멤버 제거 요청
    public void sendRemoveRunningQueue(String resourceId, String memberId) {
        CouponIssuedRemoveAtRunningQueueMessage message = CouponIssuedRemoveAtRunningQueueMessage.of(
                resourceId,
                memberId
        );
        rabbitmqProducer.sendMessage(exchangeName, routingKey, message);
    }
}
