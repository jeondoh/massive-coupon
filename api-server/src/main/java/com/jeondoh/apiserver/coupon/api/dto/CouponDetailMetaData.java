package com.jeondoh.apiserver.coupon.api.dto;

import com.jeondoh.apiserver.coupon.domain.model.CouponDetailHash;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetailRedisKeys;
import com.jeondoh.core.common.util.TimeHelper;

import java.time.LocalDateTime;

public record CouponDetailMetaData(
        String couponDetailId,
        String memberId,
        Long publishedAtToSecond,
        Long expiredAtToSecond,
        Long totalQuantity,
        CouponDetailRedisKeys keys
) {
    public static CouponDetailMetaData of(RegisterCouponDetailRequest request, String memberId, String couponDetailId) {
        LocalDateTime startDate = request.startDate() != null ? request.startDate() : LocalDateTime.now();
        return new CouponDetailMetaData(
                couponDetailId,
                memberId,
                TimeHelper.dateTimeToEpochSecond(startDate),
                TimeHelper.dateTimeToEpochSecond(request.expiredAt()),
                request.quantity(),
                CouponDetailRedisKeys.of(couponDetailId)
        );
    }

    public static CouponDetailMetaData of(CouponDetailHash couponDetailHash, String memberId) {
        return new CouponDetailMetaData(
                couponDetailHash.getCouponDetailId(),
                memberId,
                couponDetailHash.getPublishedAt(),
                couponDetailHash.getExpiredAt(),
                couponDetailHash.getTotalQuantity(),
                CouponDetailRedisKeys.of(couponDetailHash.getCouponDetailId())
        );
    }

}
