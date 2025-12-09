package com.jeondoh.core.common.dto.coupon;

import com.jeondoh.core.common.dto.DomainType;

public record CouponIssuedRemoveAtRunningQueueMessage(
        DomainType domainType,
        String resourceId,
        String memberId
) {
    public static CouponIssuedRemoveAtRunningQueueMessage of(String resourceId, String memberId) {
        return new CouponIssuedRemoveAtRunningQueueMessage(DomainType.COUPON, resourceId, memberId);
    }
}
