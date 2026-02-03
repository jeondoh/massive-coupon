package com.jeondoh.couponissueserver.api.controller;

import com.jeondoh.core.servlet.ResponseApi;
import com.jeondoh.couponissueserver.api.dto.CouponRepublishRequest;
import com.jeondoh.couponissueserver.domain.service.CouponIssueDlqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponRepublishController {

    private final CouponIssueDlqService couponIssueDlqService;

    @PostMapping("/admin/coupon/republish")
    public ResponseApi<Void> registerCoupon(
            @Valid @RequestBody CouponRepublishRequest couponRepublishRequest
    ) {
        couponIssueDlqService.couponRepublish(couponRepublishRequest);
        return ResponseApi.ok();
    }
}
