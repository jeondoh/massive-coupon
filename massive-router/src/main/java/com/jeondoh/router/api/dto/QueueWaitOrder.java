package com.jeondoh.router.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueWaitOrder(
        String memberId,
        DomainType domain,
        String resourceId
) {
    public static QueueWaitOrder of(String memberId, String domain, String resourceId) {
        return new QueueWaitOrder(memberId, DomainType.valueOf(domain), resourceId);
    }
}
