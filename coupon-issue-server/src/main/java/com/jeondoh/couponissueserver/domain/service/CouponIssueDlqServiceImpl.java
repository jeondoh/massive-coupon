package com.jeondoh.couponissueserver.domain.service;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.couponissueserver.api.dto.CouponRepublishRequest;

public interface CouponIssueDlqServiceImpl {

    // 유저 쿠폰 발급 실패 DLQ 저장
    void couponIssueFailedDLQ(CouponIssuePayload couponIssuePayload);

    // 쿠폰 재발급
    void couponRepublish(CouponRepublishRequest couponRepublishRequest);
}
