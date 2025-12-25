package com.jeondoh.apiserver.coupon.domain.model;

import com.jeondoh.apiserver.core.model.BaseEntity;
import com.jeondoh.apiserver.coupon.api.dto.CouponStatus;
import com.jeondoh.apiserver.coupon.api.dto.DiscountPolicy;
import com.jeondoh.apiserver.coupon.domain.exception.CouponException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_coupon_member_detail",
                columnNames = {"memberId", "coupon_detail_id"}
        )
})
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_detail_id")
    private CouponDetail couponDetail;

    private Long issueOrder;

    private LocalDateTime usedAt;

    // 쿠폰 발급
    public static Coupon issuedCoupon(String memberId, Long order, CouponDetail couponDetail) {
        return Coupon.builder()
                .memberId(Long.parseLong(memberId))
                .status(CouponStatus.ISSUED)
                .couponDetail(couponDetail)
                .issueOrder(order)
                .build();
    }

    // 쿠폰 사용
    // 최종금액(원가 - 할인가) 반환
    public Long useCoupon(Long originalPrice) {
        // 쿠폰 검증
        validateCoupon();
        // 최종금액 계산
        Long finalPrice = calculateFinalPrice(originalPrice);
        // 상태변경
        status = CouponStatus.USED;
        usedAt = LocalDateTime.now();

        return finalPrice;
    }

    // 할인금액 계산
    // 최종금액(원가 - 할인가) 반환
    private Long calculateFinalPrice(Long originalPrice) {
        DiscountPolicy discountPolicy = couponDetail.getDiscountPolicy();
        Long discountPrice = discountPolicy.calculateDiscountPrice(originalPrice);
        return originalPrice - discountPrice;
    }

    // 쿠폰 검증
    private void validateCoupon() {
        // 발급 상태가 아닐 경우 사용 불가
        if (CouponStatus.USED.equals(status)) {
            throw CouponException.alreadyUsedException();
        }
        if (CouponStatus.EXPIRED.equals(status) || LocalDateTime.now().isAfter(couponDetail.getExpiredAt())) {
            throw CouponException.expiredException();
        }
    }

}
