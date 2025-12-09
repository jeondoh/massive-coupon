package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;

public record SearchCouponParams(
        @ValidEnum(enumClass = CouponStatus.class)
        String status
) {
}
