package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.RegisterCouponDetailResponse;
import com.jeondoh.apiserver.coupon.api.dto.SearchCouponDetailParams;
import com.jeondoh.apiserver.coupon.api.dto.SearchCouponDetailResponse;
import com.jeondoh.apiserver.coupon.api.dto.SearchEventCouponPageResponse;
import com.jeondoh.core.servlet.PagingResponse;
import com.jeondoh.core.servlet.dto.RegisterCouponDetailRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CouponDetailService {

    // 선착순 쿠폰조회
    SearchEventCouponPageResponse searchEventCoupon(Pageable pageable, Sort sort, String memberId);

    // 관리자 쿠폰조회
    PagingResponse<SearchCouponDetailResponse> searchAdminCoupon(
            Pageable pageable, Sort sort, SearchCouponDetailParams searchCouponDetailParams
    );

    // 관리자 쿠폰등록
    RegisterCouponDetailResponse registerCoupon(String memberId, RegisterCouponDetailRequest registerCouponDetailRequest);
}
