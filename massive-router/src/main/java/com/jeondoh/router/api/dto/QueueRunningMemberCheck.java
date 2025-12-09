package com.jeondoh.router.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueRunningMemberCheck(
        DomainType domain,
        String resourceId,
        String memberId
) {
    public static QueueRunningMemberCheck of(String domain, String resourceId, String memberId) {
        return new QueueRunningMemberCheck(DomainType.valueOf(domain), resourceId, memberId);
    }
}
