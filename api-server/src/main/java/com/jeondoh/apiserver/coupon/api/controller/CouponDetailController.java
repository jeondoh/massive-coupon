package com.jeondoh.apiserver.coupon.api.controller;

import com.jeondoh.apiserver.coupon.api.dto.RegisterCouponDetailResponse;
import com.jeondoh.apiserver.coupon.api.dto.SearchCouponDetailParams;
import com.jeondoh.apiserver.coupon.api.dto.SearchCouponDetailResponse;
import com.jeondoh.apiserver.coupon.api.dto.SearchEventCouponPageResponse;
import com.jeondoh.apiserver.coupon.domain.service.CouponDetailService;
import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.servlet.PagingResponse;
import com.jeondoh.core.servlet.ResponseApi;
import com.jeondoh.core.servlet.dto.RegisterCouponDetailRequest;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponDetailController {

    private final JwtDecoder jwtDecoder;
    private final CouponDetailService couponDetailService;

    // 선착순 쿠폰 조회
    @GetMapping("/event/coupon/{resourceId}")
    public ResponseApi<SearchEventCouponPageResponse> searchEventCoupon(
            HttpServletRequest httpServletRequest,
            @PathVariable String resourceId,
            @PageableDefault Pageable pageable,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort
    ) {
        JwtToken jwtToken = jwtDecoder.decode(httpServletRequest.getHeader(AUTH_HEADER_PREFIX_KEY));
        SearchEventCouponPageResponse eventCoupon
                = couponDetailService.searchEventCoupon(pageable, sort, jwtToken.memberId());
        return ResponseApi.ok(eventCoupon);
    }

    // 관리자 쿠폰조회
    @GetMapping("/admin/coupon")
    public ResponseApi<PagingResponse<SearchCouponDetailResponse>> searchAdminCoupon(
            @PageableDefault Pageable pageable,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort,
            @Valid SearchCouponDetailParams searchCouponDetailParams
    ) {
        PagingResponse<SearchCouponDetailResponse> couponDetail
                = couponDetailService.searchAdminCoupon(pageable, sort, searchCouponDetailParams);
        return ResponseApi.ok(couponDetail);
    }

    // 관리자 쿠폰등록
    @PostMapping("/admin/coupon")
    public ResponseApi<RegisterCouponDetailResponse> registerCoupon(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody RegisterCouponDetailRequest registerCouponDetailRequest
    ) {
        JwtToken jwtToken = jwtDecoder.decode(httpServletRequest.getHeader(AUTH_HEADER_PREFIX_KEY));
        RegisterCouponDetailResponse registerCouponDetailResponse
                = couponDetailService.registerCoupon(jwtToken.memberId(), registerCouponDetailRequest);
        return ResponseApi.ok(registerCouponDetailResponse);
    }

}
