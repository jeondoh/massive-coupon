package com.jeondoh.domainqueue.infrastructure.config;

import com.jeondoh.queuecore.exception.QueueConfigException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.List;

@Configuration
public class RedisLuaScripts {

    // 대기열 진입
    // 반환값 {status, rank, total}
    // - status: 참여여부 -1(이미 참여중), 0(이미 대기중), 1(대기열 진입 성공)
    // - rank: 대기 순번
    // - total: 전체 대기자 수
    @Bean
    public RedisScript<List> enterQueue() {
        return getRedisScript("enter-queue.lua", List.class);
    }

    // Waiting → Running 이동
    // 반환값: 이동된 멤버 리스트
    @Bean
    public RedisScript<List> transferJob() {
        return getRedisScript("transfer-job.lua", List.class);
    }

    // 여러 멤버의 대기순번 및 대기자 수 조회
    // 반환값: List (성공여부, 대기 순번, 전체 대기자 수)
    @Bean
    public RedisScript<List> waitOrder() {
        return getRedisScript("wait-order.lua", List.class);
    }

    // running score 값 업데이트
    @Bean
    public RedisScript<Long> updateRunningScore() {
        return getRedisScript("update-running-score.lua", Long.class);
    }

    // timeout된 멤버를 running queue에서 제거
    // 반환값: 제거된 멤버 리스트
    @Bean
    public RedisScript<List> extractMember() {
        return getRedisScript("extract-member.lua", List.class);
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
