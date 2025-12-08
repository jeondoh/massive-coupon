package com.jeondoh.domainqueue.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueExtractRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<List> extractMember;

    // timeout된 멤버를 running queue에서 제거
    // - 현재시간보다 지정시간 이상 오래된 score를 가진 멤버 제거
    // - 토큰도 함께 삭제
    public List<String> extractTimeoutMembers(String runningKey, long timeoutScore, String tokenKeyPrefix) {
        return redisTemplate.execute(
                extractMember,
                List.of(runningKey),
                String.valueOf(timeoutScore),
                tokenKeyPrefix
        );
    }
}
