package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.apiserver.coupon.domain.exception.CouponException;
import com.jeondoh.apiserver.coupon.domain.model.Coupon;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetail;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponDetailRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponSearchQueryDslRepository;
import com.jeondoh.core.servlet.PagingResponse;
import lombok.RequiredArgsConstructor;
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
    private final CouponDetailRepository couponDetailRepository;
    private final CouponSearchQueryDslRepository couponSearchQueryDslRepository;

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
    public IssueCouponResponse issueCoupon(IssueCouponRequest issueCouponRequest) {
        String memberId = issueCouponRequest.memberId();
        Long couponDetailId = issueCouponRequest.couponDetailId();

        // 쿠폰 발급 중복체크
        Long duplicateCount = couponRepository.findDuplicateIssuedCouponWithLock(couponDetailId, Long.parseLong(memberId));
        if (duplicateCount > 0) {
            throw CouponException.duplicateIssueException();
        }

        CouponDetail couponDetail = couponDetailRepository.findByIdWithLock(couponDetailId)
                .orElseThrow(CouponException::notFoundException);

        // 발행일자 이전에 발급 요청시 예외
        couponDetail.validatePublished();

        // 쿠폰 발급
        Coupon coupon = Coupon.issuedCoupon(memberId, couponDetail);
        Coupon savedCoupon = couponRepository.save(coupon);

        // 쿠폰 재고 차감
        couponDetail.decreaseRemainQuantity();

        return IssueCouponResponse.of(savedCoupon.getId(), savedCoupon.getStatus());
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
