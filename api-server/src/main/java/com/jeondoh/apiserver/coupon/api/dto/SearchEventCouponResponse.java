package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.core.servlet.dto.DiscountPolicy;

import java.time.LocalDateTime;

public record SearchEventCouponResponse(
        Long couponDetailId,
        Long quantity,
        String couponName,
        DiscountPolicy discountPolicy,
        LocalDateTime publishedAt,
        LocalDateTime expiredAt
) {
}
