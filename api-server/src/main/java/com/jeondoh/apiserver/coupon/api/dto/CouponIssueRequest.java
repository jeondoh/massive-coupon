package com.jeondoh.apiserver.coupon.api.dto;

import jakarta.validation.constraints.NotNull;

public record CouponIssueRequest(
        @NotNull(message = "resourceId 필수")
        String resourceId
) {
}
