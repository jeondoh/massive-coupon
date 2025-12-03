package com.jeondoh.router.infrastructure.config;

import com.jeondoh.queuecore.exception.QueueConfigException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;

@Configuration
public class RedisLuaScripts {

    // config가 존재하지 않을 시 생성
    // 반환값: config 신규 생성여부 (-1: 에러, 0: 미생성, 1: 생성)
    @Bean
    public RedisScript<Long> createConfigIfNotExists() {
        return getRedisScript("create-config-not-exists.lua", Long.class);
    }

    // 대기열 필요 여부
    // - Waiting/Running/Traffic 체크
    // 반환값: 대기열 필요 여부 (0: 불필요, 1: 필요)
    @Bean
    public RedisScript<Long> checkQueueRequired() {
        return getRedisScript("check-queue-required.lua", Long.class);
    }

    private <T> RedisScript<T> getRedisScript(String script, Class<T> clazz) {
        String scriptSrc = "lua/" + script;
        try {
            return new DefaultRedisScript<>(
                    new ResourceScriptSource(
                            new ClassPathResource(scriptSrc)
                    ).getScriptAsString(), clazz
            );
        } catch (IOException e) {
            throw QueueConfigException.luaFileIoException();
        }
    }

}
