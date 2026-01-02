package com.jeondoh.couponissueserver.infrastructure.rabbitmq;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.core.servlet.infrastructure.rabbitmq.RabbitMQConsumer;
import com.jeondoh.couponissueserver.domain.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final RabbitMQConsumer rabbitMQConsumer;
    private final CouponIssueService couponIssueService;

    @RabbitListener(queues = "${rabbitmq.queues.coupon-issue.queue-name}")
    public void handleCouponIssue(@Payload CouponIssuePayload couponIssuePayload) {
        rabbitMQConsumer.consume(couponIssuePayload, couponIssueService::couponIssued);
    }
}
