package com.jeondoh.core.servlet.dto;

public record CouponIssuePayload(
        String resourceId,
        String memberId,
        Long couponDetailId,
        Long order
) {
    public static CouponIssuePayload of(String resourceId, String memberId, String couponDetailId, Long order) {
        return new CouponIssuePayload(resourceId, memberId, Long.parseLong(couponDetailId), order);
    }
}
