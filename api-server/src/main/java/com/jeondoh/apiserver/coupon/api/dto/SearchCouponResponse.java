package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.core.servlet.dto.CouponStatus;
import com.jeondoh.core.servlet.dto.CouponType;
import com.jeondoh.core.servlet.dto.DiscountPolicy;
import com.jeondoh.core.servlet.model.Coupon;
import com.jeondoh.core.servlet.model.CouponDetail;

import java.time.LocalDateTime;

public record SearchCouponResponse(
        Long id,
        CouponStatus status,
        String couponName,
        CouponType couponType,
        Long discountAmount,
        Long maxDiscountAmount,
        Long minPaymentAmount,
        LocalDateTime publishedAt,
        LocalDateTime expiredAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {
    public static SearchCouponResponse from(Coupon coupon) {
        CouponDetail couponDetail = coupon.getCouponDetail();
        DiscountPolicy discountPolicy = couponDetail.getDiscountPolicy();

        return new SearchCouponResponse(
                coupon.getId(),
                coupon.getStatus(),
                couponDetail.getCouponName(),
                discountPolicy.getCouponType(),
                discountPolicy.getDiscountAmount(),
                discountPolicy.getMaxDiscountAmount(),
                discountPolicy.getMinPaymentAmount(),
                couponDetail.getPublishedAt(),
                couponDetail.getExpiredAt(),
                coupon.getUsedAt(),
                coupon.getCreatedAt()
        );
    }
}
