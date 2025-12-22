package com.jeondoh.apiserver.coupon.domain.service;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.apiserver.coupon.domain.exception.CouponException;
import com.jeondoh.apiserver.coupon.domain.model.Coupon;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetail;
import com.jeondoh.apiserver.coupon.infrastructure.rabbitmq.CouponQueueRemoveRabbitmqSender;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponDetailRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponRepository;
import com.jeondoh.apiserver.coupon.infrastructure.repository.CouponSearchQueryDslRepository;
import com.jeondoh.core.servlet.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final CouponQueueRemoveRabbitmqSender couponQueueRemoveRabbitmqSender;

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

        CouponDetail couponDetail = couponDetailRepository.findById(couponDetailId)
                .orElseThrow(CouponException::notFoundException);

        // 발행일자 검증
        couponDetail.validatePublished();

        // 재고 차감
        int updated = couponDetailRepository.decreaseRemainQuantity(couponDetailId);
        if (updated == 0) {
            throw CouponException.soldOutException();
        }

        // 쿠폰 발급
        Coupon coupon = Coupon.issuedCoupon(memberId, couponDetail);
        try {
            Coupon savedCoupon = couponRepository.save(coupon);
            return IssueCouponResponse.of(savedCoupon.getId(), savedCoupon.getStatus());
        } catch (DataIntegrityViolationException e) {
            // 유니크 제약 위반, 중복 발행 예외
            throw CouponException.duplicateIssueException();
        }
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
