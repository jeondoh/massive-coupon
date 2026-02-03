package com.jeondoh.couponissueserver.api.dto;

import jakarta.validation.constraints.NotNull;

public record CouponRepublishRequest(
        @NotNull(message = "dlq 아이디 필수")
        Long dlqId
) {
}
