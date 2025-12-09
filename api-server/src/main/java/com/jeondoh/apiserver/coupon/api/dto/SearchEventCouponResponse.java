package com.jeondoh.apiserver.coupon.api.dto;

import java.time.LocalDateTime;

public record SearchEventCouponResponse(
        Long couponDetailId,
        Long quantity,
        Long remainQuantity,
        String couponName,
        DiscountPolicy discountPolicy,
        Boolean isAlreadyIssued,
        LocalDateTime publishedAt,
        LocalDateTime expiredAt
) {
}
