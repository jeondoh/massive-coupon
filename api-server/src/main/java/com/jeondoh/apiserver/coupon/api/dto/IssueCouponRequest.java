package com.jeondoh.apiserver.coupon.api.dto;

import jakarta.validation.constraints.NotNull;

public record IssueCouponRequest(
        @NotNull(message = "쿠폰 아이디 필수")
        Long couponDetailId,
        @NotNull(message = "유저 아이디 필수")
        String memberId,
        @NotNull(message = "resourceId 필수")
        String resourceId

) {
    public static IssueCouponRequest of(Long couponDetailId, String memberId, String resourceId) {
        return new IssueCouponRequest(couponDetailId, memberId, resourceId);
    }
}
