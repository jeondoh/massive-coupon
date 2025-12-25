package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.IssueCouponEvent;
import com.jeondoh.apiserver.coupon.domain.model.Coupon;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetail;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssueServiceImpl implements CouponIssueService {

    private final EntityManager entityManager;
    private final CouponRepository couponRepository;

    // 유저 쿠폰 발급
    @Override
    @Transactional
    public void saveCouponIssued(IssueCouponEvent issueCouponEvent) {
        CouponDetail couponDetailRef = entityManager.getReference(
                CouponDetail.class,
                issueCouponEvent.couponDetailId()
        );
        // 쿠폰 발급
        Coupon coupon = Coupon.issuedCoupon(issueCouponEvent.memberId(), issueCouponEvent.order(), couponDetailRef);
        couponRepository.save(coupon);
    }
}
