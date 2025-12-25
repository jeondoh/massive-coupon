package com.jeondoh.apiserver.coupon.api.dto;

import static com.jeondoh.apiserver.core.util.StaticVariables.*;

public record IssueCoupon(
        String memberId,
        String couponDetailId,
        String couponDetailKey,
        String issuedQuantityKey,
        String issuedMemberKey
) {
    public static IssueCoupon of(String memberId, String couponDetailId) {
        return new IssueCoupon(
                memberId,
                couponDetailId,
                COUPON_DETAIL_KEY + ":" + couponDetailId,
                COUPON_ISSUED_QUANTITY_KEY + ":" + couponDetailId,
                COUPON_ISSUED_MEMBER_KEY + ":" + couponDetailId
        );
    }
}
