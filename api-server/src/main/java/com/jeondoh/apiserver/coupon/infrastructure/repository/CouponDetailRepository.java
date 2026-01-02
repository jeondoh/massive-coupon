package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.core.servlet.model.CouponDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDetailRepository extends JpaRepository<CouponDetail, Long> {
}
