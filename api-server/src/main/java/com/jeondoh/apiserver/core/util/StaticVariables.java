package com.jeondoh.apiserver.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticVariables {

    // ========================================================================
    // Redis KEY값
    public static final String COUPON_DETAIL_KEY = "coupon:detail";
    public static final String COUPON_ISSUED_QUANTITY_KEY = "coupon:issued:quantity";
    public static final String COUPON_ISSUED_MEMBER_KEY = "coupon:issued:member";
}
