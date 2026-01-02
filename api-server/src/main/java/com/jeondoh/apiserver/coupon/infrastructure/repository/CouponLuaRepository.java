package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.api.dto.CouponDetailMetaData;
import com.jeondoh.core.servlet.exception.CouponException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponLuaRepository {

    private final RedisScript<Long> issueCouponScript;
    private final StringRedisTemplate stringRedisTemplate;

    // 쿠폰 발급 검증, 발행 처리
    // - 발급 순번 반환
    public Long issuedCoupon(CouponDetailMetaData couponDetailMetaData) {
        List<String> keys = couponDetailMetaData.keys().makeListArrays();
        Long execute = stringRedisTemplate.execute(
                issueCouponScript,
                keys,
                couponDetailMetaData.memberId(),
                couponDetailMetaData.totalQuantity().toString()
        );
        // 결과에 따라 예외처리
        if (execute == -1L) {
            // 중복 발급
            throw CouponException.duplicateIssueException("memberId: " + couponDetailMetaData.memberId());
        } else if (execute == 0L) {
            // 재고 없음, 매진
            throw CouponException.soldOutException();
        }
        return execute;
    }
}
