package com.jeondoh.apiserver.coupon.domain.event;

import com.jeondoh.apiserver.coupon.api.dto.IssueCouponEvent;
import com.jeondoh.apiserver.coupon.domain.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CouponIssueEventListener {

    private final CouponIssueService couponIssueService;

    @Async("vt_coupon_issue_executor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCouponIssueEvent(IssueCouponEvent issueCouponEvent) {
        couponIssueService.saveCouponIssued(issueCouponEvent);
    }
}
