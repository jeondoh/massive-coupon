package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.api.dto.CouponDetailCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jeondoh.apiserver.core.util.StaticVariables.*;

@Repository
public class CouponRedisRepository {

    public CouponRedisRepository(
            @Value("${coupon.detail.hash-timeout-days}") int couponDetailTimeoutDays,
            StringRedisTemplate stringRedisTemplate
    ) {
        this.couponDetailTimeoutDays = couponDetailTimeoutDays;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private final int couponDetailTimeoutDays;
    private final StringRedisTemplate stringRedisTemplate;

    // 쿠폰 정보 등록
    public void registerCouponDetail(CouponDetailCache couponDetailCache) {
        // 만료 시간 지정
        long ttl = Duration.ofDays(couponDetailTimeoutDays).toSeconds();

        // 쿠폰 정보 저장
        String detailKey = COUPON_DETAIL_KEY + ":" + couponDetailCache.couponDetailId();
        stringRedisTemplate.opsForHash().putAll(
                detailKey,
                Map.of(
                        "publishedAt", couponDetailCache.publishedAtToSecond(),
                        "expiredAt", couponDetailCache.expiredAtToSecond(),
                        "totalQuantity", couponDetailCache.totalQuantity()
                )
        );
        stringRedisTemplate.expire(detailKey, ttl, TimeUnit.SECONDS);

        // 쿠폰 재고 저장
        stringRedisTemplate.opsForValue().set(
                COUPON_ISSUED_QUANTITY_KEY + ":" + couponDetailCache.couponDetailId(),
                "0",
                ttl,
                TimeUnit.SECONDS
        );

        // 중복발급 방지용
        String memberKey = COUPON_ISSUED_MEMBER_KEY + ":" + couponDetailCache.couponDetailId();
        stringRedisTemplate.opsForSet().add(
                memberKey,
                "0"
        );
        stringRedisTemplate.expire(memberKey, ttl, TimeUnit.SECONDS);
    }


}
