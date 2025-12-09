package com.jeondoh.apiserver.coupon.api.dto;

import jakarta.validation.constraints.NotNull;

public record UseCouponRequest(
        @NotNull(message = "원가 필수")
        Long originalPrice
) {
}
