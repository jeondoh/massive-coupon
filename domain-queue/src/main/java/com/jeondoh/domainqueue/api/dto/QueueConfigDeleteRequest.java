package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.core.common.dto.DomainType;

public record QueueConfigDeleteRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        String resourceId
) {
}
