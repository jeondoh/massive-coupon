package com.jeondoh.apiserver.coupon.infrastructure.rabbitmq;

import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.core.common.infrastructure.rabbitmq.RabbitmqSendHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CouponQueueRemoveRabbitMqSenderImpl implements CouponQueueRemoveRabbitmqSender {

    public CouponQueueRemoveRabbitMqSenderImpl(
            RabbitmqSendHelper rabbitmqSendHelper,
            @Value("${spring.rabbitmq.queues.coupon-queue-remove.exchange-name}") String exchangeName,
            @Value("${spring.rabbitmq.queues.coupon-queue-remove.routing-key}") String routingKey
    ) {
        this.rabbitmqSendHelper = rabbitmqSendHelper;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    private final RabbitmqSendHelper rabbitmqSendHelper;
    private final String exchangeName;
    private final String routingKey;

    // running queue 멤버 제거 요청
    @Override
    public void sendRemoveRunningQueue(String resourceId, String memberId) {
        CouponIssuedRemoveAtRunningQueueMessage message = CouponIssuedRemoveAtRunningQueueMessage.of(
                resourceId,
                memberId
        );
        rabbitmqSendHelper.sendMessage(exchangeName, routingKey, message);
    }
}
