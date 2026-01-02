package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.core.servlet.dto.DiscountPolicy;

import java.time.LocalDateTime;

public record SearchCouponDetailResponse(
        Long couponDetailId,
        Long quantity,
        Long remainQuantity,
        String couponName,
        DiscountPolicy discountPolicy,
        LocalDateTime publishAt,
        LocalDateTime expiredAt
) {
}
