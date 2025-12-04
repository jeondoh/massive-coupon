package com.jeondoh.domainqueue.api.dto;


import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.queuecore.domain.DomainType;

public record QueueConfigUpdateRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        String resourceId,
        String field,
        String value
) {
}
