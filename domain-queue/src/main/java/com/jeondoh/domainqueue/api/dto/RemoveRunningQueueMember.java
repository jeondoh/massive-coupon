package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.queuecore.domain.QueueType;

public record RemoveRunningQueueMember(
        String runningKey,
        String memberId
) {
    public static RemoveRunningQueueMember from(CouponIssuedRemoveAtRunningQueueMessage message) {
        String runningKey = QueueType.RUNNING.getKey(message.domainType(), message.resourceId());
        return new RemoveRunningQueueMember(runningKey, message.memberId());
    }

}
