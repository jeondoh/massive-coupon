package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.core.common.annotation.ValidEnum;
import com.jeondoh.core.servlet.dto.CouponStatus;

public record SearchCouponParams(
        @ValidEnum(enumClass = CouponStatus.class)
        String status
) {
}
