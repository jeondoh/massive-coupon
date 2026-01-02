package com.jeondoh.apiserver.coupon.domain.model;

import com.jeondoh.apiserver.coupon.api.dto.CouponDetailMetaData;
import com.jeondoh.core.servlet.exception.CouponException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import static com.jeondoh.apiserver.core.util.StaticVariables.COUPON_DETAIL_KEY;

@Getter
@Builder
@RedisHash(value = COUPON_DETAIL_KEY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDetailHash {

    @Id
    private String couponDetailId;

    private Long publishedAt;

    private Long expiredAt;

    private Long totalQuantity;

    @TimeToLive
    private Long timeToLive;

    public static CouponDetailHash of(CouponDetailMetaData metaData, Long ttl) {
        return CouponDetailHash.builder()
                .couponDetailId(metaData.couponDetailId())
                .publishedAt(metaData.publishedAtToSecond())
                .expiredAt(metaData.expiredAtToSecond())
                .totalQuantity(metaData.totalQuantity())
                .timeToLive(ttl)
                .build();
    }

    // 쿠폰 발행일 & 만기일 검증
    public void validateCoupon() {
        // 현재시간(초)
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime < publishedAt || currentTime > expiredAt) {
            // 이벤트 일자 아님
            throw CouponException.notEventTimeException();
        }
    }
}
