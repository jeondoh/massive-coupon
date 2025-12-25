package com.jeondoh.apiserver.coupon.infrastructure.config;

import com.jeondoh.core.common.component.RedisLuaScriptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
@RequiredArgsConstructor
public class RedisLuaScripts {

    private final RedisLuaScriptHelper redisLuaScriptHelper;

    // 쿠폰 발급 검증, 발행 처리
    // 반환값
    // - status: -3(쿠폰 상세 미등록), -2(이벤트 일자가 아님), -1(중복 발급한 유저), 0(재고 없음), 그 외 발급 순번 반환
    @Bean
    public RedisScript<Long> issueCoupon() {
        return redisLuaScriptHelper.getRedisScript("issue-coupon.lua", Long.class);
    }

}
