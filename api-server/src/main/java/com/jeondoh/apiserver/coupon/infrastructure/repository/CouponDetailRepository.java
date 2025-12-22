package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.domain.model.CouponDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponDetailRepository extends JpaRepository<CouponDetail, Long> {

    // 재고 차감
    // - 성공시 1, 실패(품절)시 0
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update CouponDetail cd
            set cd.remainQuantity = cd.remainQuantity - 1
            where cd.id = :couponDetailId and cd.remainQuantity > 0
            """)
    int decreaseRemainQuantity(@Param("couponDetailId") Long couponDetailId);
}
