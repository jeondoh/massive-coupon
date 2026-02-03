package com.jeondoh.couponissueserver.domain.service;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.couponissueserver.api.dto.CouponRepublishRequest;
import com.jeondoh.couponissueserver.domain.exception.CouponIssueException;
import com.jeondoh.couponissueserver.domain.model.CouponIssueDLQ;
import com.jeondoh.couponissueserver.infrastructure.rabbitmq.CouponRepublishProducer;
import com.jeondoh.couponissueserver.infrastructure.repository.CouponIssueDLQRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponIssueDlqService implements CouponIssueDlqServiceImpl {

    public CouponIssueDlqService(
            CouponIssueDLQRepository couponIssueDLQRepository,
            CouponRepublishProducer couponIssueProducer,
            @Value("${rabbitmq.queues.coupon-issue.dlq.name}") String queueName
    ) {
        this.couponIssueDLQRepository = couponIssueDLQRepository;
        this.couponRepublishProducer = couponIssueProducer;
        this.queueName = queueName;
    }

    private final String queueName;
    private final CouponRepublishProducer couponRepublishProducer;
    private final CouponIssueDLQRepository couponIssueDLQRepository;

    // 유저 쿠폰 발급 실패 DLQ 저장
    @Override
    @Transactional
    public void couponIssueFailedDLQ(CouponIssuePayload couponIssuePayload) {
        CouponIssueDLQ couponIssueDLQ = CouponIssueDLQ.of(couponIssuePayload, queueName);
        couponIssueDLQRepository.save(couponIssueDLQ);
    }

    @Override
    @Transactional
    public void couponRepublish(CouponRepublishRequest couponRepublishRequest) {
        CouponIssueDLQ couponIssueDLQ = couponIssueDLQRepository.findById(couponRepublishRequest.dlqId())
                .orElseThrow(CouponIssueException::notFoundCouponException);

        // 유저 쿠폰 발급 요청
        CouponIssuePayload couponIssuePayload = CouponIssuePayload.of(
                couponIssueDLQ.getResourceId(),
                couponIssueDLQ.getMemberId(),
                couponIssueDLQ.getCouponDetailId().toString(),
                couponIssueDLQ.getOrder()
        );
        couponRepublishProducer.sendMessage(couponIssuePayload);
    }
}
