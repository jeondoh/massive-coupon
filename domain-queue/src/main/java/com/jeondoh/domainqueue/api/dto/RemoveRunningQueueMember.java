package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.dto.DomainType;
import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.queuecore.domain.QueueType;

public record RemoveRunningQueueMember(
        String runningKey,
        String domainKey
) {
    public static RemoveRunningQueueMember from(CouponIssuedRemoveAtRunningQueueMessage message) {
        DomainType domain = message.domainType();
        String resourceId = message.resourceId();
        String memberId = message.memberId();
        String runningKey = QueueType.RUNNING.getKey(domain, resourceId);
        String domainKey = domain.getDomainKey(resourceId, memberId);
        return new RemoveRunningQueueMember(runningKey, domainKey);
    }

}
