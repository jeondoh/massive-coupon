package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.queuecore.domain.DomainType;
import jakarta.validation.constraints.NotNull;

public record QueueHeartbeatRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        @NotNull(message = "id값 설정은 필수")
        String resourceId
) {
}
