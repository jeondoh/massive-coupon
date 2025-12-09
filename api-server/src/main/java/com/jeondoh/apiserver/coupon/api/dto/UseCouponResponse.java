package com.jeondoh.apiserver.coupon.api.dto;

public record UseCouponResponse(
        Long originalPrice,
        Long discountPrice,
        Long finalPrice
) {
    public static UseCouponResponse of(Long originalPrice, Long discountPrice, Long finalPrice) {
        return new UseCouponResponse(originalPrice, discountPrice, finalPrice);
    }
}
