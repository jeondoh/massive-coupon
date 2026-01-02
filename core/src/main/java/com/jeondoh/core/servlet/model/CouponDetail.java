package com.jeondoh.core.servlet.model;

import com.jeondoh.core.servlet.dto.CouponType;
import com.jeondoh.core.servlet.dto.DiscountPolicy;
import com.jeondoh.core.servlet.dto.RegisterCouponDetailRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_detail_id")
    private Long id;

    // 발행 수량
    private Long quantity;

    // 남은 수량
    private Long remainQuantity;

    // 쿠폰명
    private String couponName;

    // 할인 정책
    @Embedded
    private DiscountPolicy discountPolicy;

    // 발행 담당 관리자
    private Long publisherId;

    // 발행일자
    private LocalDateTime publishedAt;

    // 만료일자
    private LocalDateTime expiredAt;

    // RegisterCouponDetailRequest DTO -> Entity 변환
    public static CouponDetail of(RegisterCouponDetailRequest request, String publisherId) {
        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .couponType(CouponType.valueOf(request.couponType()))
                .discountAmount(request.discountAmount())
                .maxDiscountAmount(request.maxDiscountAmount() != null ? request.maxDiscountAmount() : 0L)
                .minPaymentAmount(request.minPaymentAmount() != null ? request.minPaymentAmount() : 0L)
                .build();

        return CouponDetail.builder()
                .quantity(request.quantity())
                .remainQuantity(request.quantity())
                .couponName(request.couponName())
                .discountPolicy(discountPolicy)
                .publisherId(Long.parseLong(publisherId))
                .publishedAt(request.startDate() != null ? request.startDate() : LocalDateTime.now())
                .expiredAt(request.expiredAt())
                .build();
    }

}
