package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.core.servlet.model.Coupon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // CouponDetail까지 함께 조회
    @EntityGraph(attributePaths = {"couponDetail"})
    Optional<Coupon> findWithCouponDetailById(Long couponId);
}
