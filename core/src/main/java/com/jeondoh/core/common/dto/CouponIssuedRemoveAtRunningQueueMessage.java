package com.jeondoh.core.common.dto;

public record CouponIssuedRemoveAtRunningQueueMessage(
        DomainType domainType,
        String resourceId,
        String memberId
) {
    public static CouponIssuedRemoveAtRunningQueueMessage of(String resourceId, String memberId) {
        return new CouponIssuedRemoveAtRunningQueueMessage(DomainType.COUPON, resourceId, memberId);
    }
}
