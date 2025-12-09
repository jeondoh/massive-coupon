package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetail;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponDetailRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponDetailSearchQueryDslRepository;
import com.jeondoh.core.servlet.PagingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponDetailServiceImpl implements CouponDetailService {

    private final CouponDetailRepository couponDetailRepository;
    private final CouponDetailSearchQueryDslRepository couponDetailSearchQueryDslRepository;

    // 선착순 쿠폰조회
    @Override
    public PagingResponse<SearchEventCouponResponse> searchEventCoupon(Pageable pageable, Sort sort, String memberId) {
        // 이벤트 쿠폰 목록 조회
        Page<SearchEventCouponResponse> eventCoupon
                = couponDetailSearchQueryDslRepository.findByEventCoupon(pageable, sort, memberId);

        return PagingResponse.from(eventCoupon);
    }

    // 관리자 쿠폰조회
    @Override
    public PagingResponse<SearchCouponDetailResponse> searchAdminCoupon(
            Pageable pageable,
            Sort sort,
            SearchCouponDetailParams searchCouponDetailParams
    ) {
        // 쿠폰 목록 조회
        Page<SearchCouponDetailResponse> couponDetail = couponDetailSearchQueryDslRepository.findByCouponDetailParams(
                pageable,
                sort,
                searchCouponDetailParams
        );

        return PagingResponse.from(couponDetail);
    }

    // 관리자 쿠폰등록
    @Override
    @Transactional
    public RegisterCouponDetailResponse registerCoupon(
            String memberId,
            RegisterCouponDetailRequest registerCouponDetailRequest
    ) {
        CouponDetail couponDetail = CouponDetail.of(registerCouponDetailRequest, memberId);
        CouponDetail saveCoupon = couponDetailRepository.save(couponDetail);

        return RegisterCouponDetailResponse.of(saveCoupon.getId());
    }
}
