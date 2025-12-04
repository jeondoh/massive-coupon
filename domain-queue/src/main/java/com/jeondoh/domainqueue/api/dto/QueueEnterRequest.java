package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.queuecore.domain.DomainType;
import jakarta.validation.constraints.NotBlank;

public record QueueEnterRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        @NotBlank(message = "resourceId는 필수")
        String resourceId
) {
}
