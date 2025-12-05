package com.jeondoh.router.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.queuecore.domain.DomainType;

public record QueueWaitOrderRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        String resourceId
) {
}
