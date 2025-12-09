package com.jeondoh.apiserver.coupon.api.controller;

import com.jeondoh.apiserver.coupon.api.dto.*;
import com.jeondoh.apiserver.coupon.domain.service.CouponService;
import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.servlet.PagingResponse;
import com.jeondoh.core.servlet.ResponseApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final JwtDecoder jwtDecoder;

    // 쿠폰 목록 조회
    @GetMapping
    public ResponseApi<PagingResponse<SearchCouponResponse>> searchCoupons(
            HttpServletRequest httpServletRequest,
            @PageableDefault Pageable pageable,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort,
            @Valid SearchCouponParams searchCouponParams
    ) {
        JwtToken jwtToken = jwtDecoder.decode(httpServletRequest.getHeader(AUTH_HEADER_PREFIX_KEY));
        PagingResponse<SearchCouponResponse> coupons = couponService.searchCoupons(
                pageable,
                sort,
                searchCouponParams,
                jwtToken.memberId()
        );
        return ResponseApi.ok(coupons);
    }

    // 쿠폰 발급
    @PostMapping("/{couponDetailId}")
    public ResponseApi<IssueCouponResponse> issueCoupon(
            HttpServletRequest httpServletRequest,
            @PathVariable Long couponDetailId
    ) {
        JwtToken jwtToken = jwtDecoder.decode(httpServletRequest.getHeader(AUTH_HEADER_PREFIX_KEY));
        IssueCouponRequest issueCouponRequest = IssueCouponRequest.of(couponDetailId, jwtToken.memberId());
        IssueCouponResponse issueCouponResponse = couponService.issueCoupon(issueCouponRequest);
        return ResponseApi.ok(issueCouponResponse);
    }

    // 쿠폰 사용
    @PatchMapping("/{couponId}")
    public ResponseApi<UseCouponResponse> useCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody UseCouponRequest useCouponRequest
    ) {
        UseCouponResponse useCouponResponse = couponService.useCoupon(couponId, useCouponRequest);
        return ResponseApi.ok(useCouponResponse);
    }

}
