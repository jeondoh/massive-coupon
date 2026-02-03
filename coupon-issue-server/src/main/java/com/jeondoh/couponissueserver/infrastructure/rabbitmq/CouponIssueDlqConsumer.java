package com.jeondoh.couponissueserver.infrastructure.rabbitmq;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.core.servlet.infrastructure.rabbitmq.RabbitMQConsumer;
import com.jeondoh.couponissueserver.domain.service.CouponIssueDlqService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueDlqConsumer {
    private final RabbitMQConsumer rabbitMQConsumer;
    private final CouponIssueDlqService couponIssueDlqService;

    @RabbitListener(queues = "${rabbitmq.queues.coupon-issue.dlq.name}")
    public void handleCouponIssueDlq(@Payload CouponIssuePayload couponIssuePayload) {
        rabbitMQConsumer.consume(couponIssuePayload, couponIssueDlqService::couponIssueFailedDLQ);
    }
}
