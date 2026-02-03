package com.jeondoh.couponissueserver.domain.service;

import com.jeondoh.core.common.dto.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.core.servlet.model.Coupon;
import com.jeondoh.core.servlet.model.CouponDetail;
import com.jeondoh.couponissueserver.domain.exception.CouponIssueException;
import com.jeondoh.couponissueserver.infrastructure.rabbitmq.CouponQueueRemoveProducer;
import com.jeondoh.couponissueserver.infrastructure.repository.CouponIssueRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssueServiceImpl implements CouponIssueService {

    private final EntityManager entityManager;
    private final CouponIssueRepository couponIssueRepository;
    private final CouponQueueRemoveProducer couponQueueRemoveProducer;

    // 유저 쿠폰 발급
    @Override
    @Transactional
    public void couponIssued(CouponIssuePayload couponIssuePayload) {
        CouponDetail couponDetailRef = entityManager.getReference(
                CouponDetail.class,
                couponIssuePayload.couponDetailId()
        );
        // 쿠폰 발급
        Coupon coupon = Coupon.issuedCoupon(couponIssuePayload.memberId(), couponIssuePayload.order(), couponDetailRef);
        try {
            couponIssueRepository.save(coupon);
        } catch (DataIntegrityViolationException e) {
            // 중복발급 처리
            throw CouponIssueException.duplicateCouponException(couponIssuePayload.memberId());
        }
        // running queue 멤버 제거 요청
        CouponIssuedRemoveAtRunningQueueMessage message = CouponIssuedRemoveAtRunningQueueMessage.of(
                couponIssuePayload.resourceId(),
                couponIssuePayload.memberId()
        );
        couponQueueRemoveProducer.sendMessage(message);
    }
}
