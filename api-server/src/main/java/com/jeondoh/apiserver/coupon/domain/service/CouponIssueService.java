package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.IssueCouponEvent;

public interface CouponIssueService {

    // 유저 쿠폰 발급
    void saveCouponIssued(IssueCouponEvent issueCouponEvent);
}
