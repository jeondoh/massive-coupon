package com.jeondoh.router.api.dto;

import com.jeondoh.core.common.dto.DomainType;

public record QueueConfigExists(
        DomainType domainType,
        String resourceId
) {
    public static QueueConfigExists of(String domain, String resourceId) {
        return new QueueConfigExists(DomainType.valueOf(domain), resourceId);
    }
}
