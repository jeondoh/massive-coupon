package com.jeondoh.apiserver.coupon.api.dto;

public record IssueCouponResponse(
        Long couponId,
        CouponStatus status
) {

    public static IssueCouponResponse of(Long couponId, CouponStatus status) {
        return new IssueCouponResponse(couponId, status);
    }

}
