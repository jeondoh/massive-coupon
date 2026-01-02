package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.api.dto.CouponDetailMetaData;
import com.jeondoh.apiserver.coupon.domain.model.CouponDetailHash;
import com.jeondoh.core.servlet.exception.CouponException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Repository
public class CouponDetailRedisRepository {

    public CouponDetailRedisRepository(
            @Value("${coupon.detail.hash-timeout-days}") int couponDetailTimeoutDays,
            CouponDetailCrudRepository couponDetailRedisRepository,
            StringRedisTemplate stringRedisTemplate
    ) {
        this.couponDetailTimeoutDays = couponDetailTimeoutDays;
        this.couponDetailRedisRepository = couponDetailRedisRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private final int couponDetailTimeoutDays;
    private final CouponDetailCrudRepository couponDetailRedisRepository;
    private final StringRedisTemplate stringRedisTemplate;

    // 쿠폰 정보 등록
    public void registerCouponDetail(CouponDetailMetaData couponDetailMetaData) {
        // 만료 시간 지정
        long ttl = Duration.ofDays(couponDetailTimeoutDays).toSeconds();

        // 쿠폰 정보 저장
        CouponDetailHash couponDetailHash = CouponDetailHash.of(couponDetailMetaData, ttl);
        couponDetailRedisRepository.save(couponDetailHash);

        // 쿠폰 재고 저장
        stringRedisTemplate.opsForValue().set(
                couponDetailMetaData.keys().issuedQuantityKey(),
                "0",
                ttl,
                TimeUnit.SECONDS
        );

        // 중복발급 방지용
        String memberKey = couponDetailMetaData.keys().issuedMemberKey();
        stringRedisTemplate.opsForSet().add(
                memberKey,
                "0"
        );
        stringRedisTemplate.expire(memberKey, ttl, TimeUnit.SECONDS);
    }

    // 쿠폰 상세 메타데이터(캐싱 데이터) 가져오기
    public CouponDetailHash findCouponDetailMetaData(String couponDetailId) {
        return couponDetailRedisRepository.findById(couponDetailId)
                .orElseThrow(() -> CouponException.notFoundException("id: " + couponDetailId));
    }


}
