package com.jeondoh.apiserver.coupon.api.dto;

public record RegisterCouponDetailResponse(Long couponDetailId) {

    public static RegisterCouponDetailResponse of(Long couponDetailId) {
        return new RegisterCouponDetailResponse(couponDetailId);
    }
}
