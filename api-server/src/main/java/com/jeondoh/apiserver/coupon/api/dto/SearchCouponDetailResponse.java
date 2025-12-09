package com.jeondoh.apiserver.coupon.api.dto;

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
