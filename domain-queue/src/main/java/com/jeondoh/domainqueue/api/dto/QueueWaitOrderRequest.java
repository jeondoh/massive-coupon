package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.core.common.dto.DomainType;
import jakarta.validation.constraints.NotNull;

public record QueueWaitOrderRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        @NotNull(message = "id값 설정은 필수")
        String resourceId
) {
}
