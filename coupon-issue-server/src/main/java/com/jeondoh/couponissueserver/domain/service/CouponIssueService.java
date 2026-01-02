package com.jeondoh.couponissueserver.domain.service;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;

public interface CouponIssueService {

    // 유저 쿠폰 발급
    void couponIssued(CouponIssuePayload couponIssuePayload);
}
