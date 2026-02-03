package com.jeondoh.couponissueserver.infrastructure.repository;

import com.jeondoh.couponissueserver.domain.model.CouponIssueDLQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueDLQRepository extends JpaRepository<CouponIssueDLQ, Long> {
}
