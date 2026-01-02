package com.jeondoh.couponissueserver.infrastructure.repository;

import com.jeondoh.core.servlet.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueRepository extends JpaRepository<Coupon, Long> {
}
