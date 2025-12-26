package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.apiserver.coupon.domain.exception.CouponException;
import com.jeondoh.apiserver.coupon.domain.model.Coupon;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetailHash;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponDetailRedisRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponLuaRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponSearchQueryDslRepository;
import com.jeondoh.core.servlet.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponSearchQueryDslRepository couponSearchQueryDslRepository;
    private final CouponDetailRedisRepository couponDetailRedisRepository;
    private final CouponLuaRepository couponLuaRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 쿠폰 목록 조회
    @Override
    public PagingResponse<SearchCouponResponse> searchCoupons(
            Pageable pageable,
            Sort sort,
            SearchCouponParams params,
            String memberId
    ) {
        Page<Coupon> coupons = couponSearchQueryDslRepository.findByCouponParams(pageable, sort, params, memberId);
        Page<SearchCouponResponse> couponsResponse = coupons.map(SearchCouponResponse::from);

        return PagingResponse.from(couponsResponse);
    }

    // 쿠폰 발급
    @Override
    @Transactional
    public void issueCoupon(IssueCouponRequest issueCouponRequest) {
        // 쿠폰 데이터 가져오기
        CouponDetailHash couponDetailHash = couponDetailRedisRepository.findCouponDetailMetaData(
                issueCouponRequest.couponDetailId()
        );

        // 쿠폰 일자 검증
        couponDetailHash.validateCoupon();

        // 쿠폰 발행 처리
        CouponDetailMetaData metaData = CouponDetailMetaData.of(couponDetailHash, issueCouponRequest.memberId());
        Long memberCouponOrder = couponLuaRepository.issuedCoupon(metaData);

        // 유저 쿠폰 발급 비동기 요청
        IssueCouponEvent issueCouponEvent = IssueCouponEvent.of(
                metaData.memberId(),
                metaData.couponDetailId(),
                memberCouponOrder
        );
        eventPublisher.publishEvent(issueCouponEvent);
    }

    // 쿠폰 사용
    @Override
    @Transactional
    public UseCouponResponse useCoupon(Long couponId, UseCouponRequest useCouponRequest) {
        Coupon coupon = couponRepository.findWithCouponDetailById(couponId)
                .orElseThrow(CouponException::notFoundException);

        // 쿠폰 사용
        Long originalPrice = useCouponRequest.originalPrice();
        Long finalPrice = coupon.useCoupon(originalPrice);

        return UseCouponResponse.of(originalPrice, originalPrice - finalPrice, finalPrice);
    }

}
