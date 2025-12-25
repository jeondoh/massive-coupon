package com.jeondoh.router.infrastructure.config;

import com.jeondoh.core.common.component.RedisLuaScriptHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
@RequiredArgsConstructor
public class RedisLuaScripts {

    private final RedisLuaScriptHelper redisLuaScriptHelper;

    // config가 존재하지 않을 시 생성
    // 반환값: config 신규 생성여부 (-1: 에러, 0: 미생성, 1: 생성)
    @Bean
    public RedisScript<Long> createConfigIfNotExists() {
        return redisLuaScriptHelper.getRedisScript("create-config-not-exists.lua", Long.class);
    }

    // 대기열 필요 여부
    // - Waiting/Running/Traffic 체크
    // 반환값: 대기열 필요 여부 (0: 불필요, 1: 필요)
    @Bean
    public RedisScript<Long> checkQueueRequired() {
        return redisLuaScriptHelper.getRedisScript("check-queue-required.lua", Long.class);
    }

}
