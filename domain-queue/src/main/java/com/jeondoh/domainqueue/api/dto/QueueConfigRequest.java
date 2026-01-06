package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.core.common.dto.DomainType;
import jakarta.validation.constraints.NotNull;

public record QueueConfigRequest(
        @ValidEnum(enumClass = DomainType.class)
        String domain,
        @NotNull(message = "id값 설정은 필수")
        String resourceId,
        @NotNull(message = "임계값 설정은 필수")
        Integer threshold,
        @NotNull(message = "이동 크기 설정은 필수")
        Integer transferSize,
        @NotNull(message = "분당 요청 설정은 필수")
        Integer trafficRpm
) {
}
