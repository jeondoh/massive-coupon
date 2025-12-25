package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.api.dto.IssueCoupon;
import com.jeondoh.apiserver.coupon.domain.exception.CouponException;
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
    public Long issuedCoupon(IssueCoupon issueCoupon) {
        List<String> keys = List.of(
                issueCoupon.couponDetailKey(),
                issueCoupon.issuedQuantityKey(),
                issueCoupon.issuedMemberKey()
        );
        Long execute = stringRedisTemplate.execute(
                issueCouponScript,
                keys,
                issueCoupon.memberId(),
                String.valueOf(System.currentTimeMillis() / 1000)
        );
        // 결과에 따라 예외처리
        if (execute == -3L) {
            // 쿠폰 상세 미등록
            throw CouponException.notFoundException("couponDetailId: " + issueCoupon.couponDetailKey());
        } else if (execute == -2L) {
            // 이벤트 일자 아님
            throw CouponException.notEventTimeException();
        } else if (execute == -1L) {
            // 중복 발급
            throw CouponException.duplicateIssueException("memberId: " + issueCoupon.memberId());
        } else if (execute == 0L) {
            // 재고 없음, 매진
            throw CouponException.soldOutException();
        }
        return execute;
    }
}
