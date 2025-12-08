package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueDomainMember(
        String memberId,
        DomainType domain,
        String resourceId
) {
    public static QueueDomainMember of(String memberId, String domain, String resourceId) {
        return new QueueDomainMember(memberId, DomainType.valueOf(domain), resourceId);
    }
}
