package com.jeondoh.apiserver.core.dto;

import com.jeondoh.apiserver.coupon.api.dto.RegisterCouponDetailRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record CouponDetailCache(
        String publisherId,
        Long couponDetailId,
        LocalDateTime publishedAt,
        LocalDateTime expiredAt,
        String publishedAtToSecond,
        String expiredAtToSecond,
        String totalQuantity
) {
    public static CouponDetailCache of(RegisterCouponDetailRequest request, long savedId, String memberId) {
        LocalDateTime startDate = request.startDate() != null ? request.startDate() : LocalDateTime.now();
        return new CouponDetailCache(
                memberId,
                savedId,
                startDate,
                request.expiredAt(),
                String.valueOf(startDate.atZone(ZoneId.systemDefault()).toEpochSecond()),
                String.valueOf(request.expiredAt().atZone(ZoneId.systemDefault()).toEpochSecond()),
                request.quantity().toString()
        );
    }

}
