package com.jeondoh.apiserver.coupon.domain.model;

import java.util.List;

import static com.jeondoh.apiserver.core.util.StaticVariables.COUPON_ISSUED_MEMBER_KEY;
import static com.jeondoh.apiserver.core.util.StaticVariables.COUPON_ISSUED_QUANTITY_KEY;

public record CouponDetailRedisKeys(
        String issuedQuantityKey,
        String issuedMemberKey
) {
    public static CouponDetailRedisKeys of(String couponDetailId) {
        return new CouponDetailRedisKeys(
                COUPON_ISSUED_QUANTITY_KEY + ":" + couponDetailId,
                COUPON_ISSUED_MEMBER_KEY + ":" + couponDetailId
        );
    }

    public List<String> makeListArrays() {
        return List.of(issuedQuantityKey, issuedMemberKey);
    }
}
