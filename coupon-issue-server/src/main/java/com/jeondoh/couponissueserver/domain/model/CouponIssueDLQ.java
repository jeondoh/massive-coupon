package com.jeondoh.couponissueserver.domain.model;

import com.jeondoh.core.servlet.dto.CouponIssuePayload;
import com.jeondoh.core.servlet.model.BaseEntity;
import com.jeondoh.couponissueserver.api.dto.CouponDLQState;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssueDLQ extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_dlq_id")
    private Long id;

    private String dlqQueueName;

    private String resourceId;

    private String memberId;

    private Long couponDetailId;

    private Long order;

    @Enumerated(EnumType.STRING)
    private CouponDLQState status;

    public static CouponIssueDLQ of(CouponIssuePayload payload, String dlqQueueName) {
        return CouponIssueDLQ.builder()
                .dlqQueueName(dlqQueueName)
                .resourceId(payload.resourceId())
                .memberId(payload.memberId())
                .couponDetailId(payload.couponDetailId())
                .order(payload.order())
                .status(CouponDLQState.NEW)
                .build();
    }

}
