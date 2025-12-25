package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.core.servlet.PagingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CouponService {

    // 쿠폰 목록 조회
    PagingResponse<SearchCouponResponse> searchCoupons(Pageable pageable, Sort sort, SearchCouponParams params, String memberId);

    // 쿠폰 발급
    void issueCoupon(IssueCouponRequest issueCouponRequest);

    // 쿠폰 사용
    UseCouponResponse useCoupon(Long couponId, UseCouponRequest useCouponRequest);
}
