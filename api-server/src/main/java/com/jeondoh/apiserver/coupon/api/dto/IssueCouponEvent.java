package com.jeondoh.apiserver.coupon.api.dto;

public record IssueCouponEvent(
        String memberId,
        Long couponDetailId,
        Long order
) {
    public static IssueCouponEvent of(String memberId, String couponDetailId, Long order) {
        return new IssueCouponEvent(memberId, Long.parseLong(couponDetailId), order);
    }
}
