package com.jeondoh.domainqueue.domain.repository;

import com.jeondoh.domainqueue.api.dto.QueueRunningScore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRunningRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> updateRunningScore;

    // running queue score 값 업데이트
    public Long updateRunningScore(QueueRunningScore runningScore) {
        long currentTime = System.currentTimeMillis();

        return redisTemplate.execute(
                updateRunningScore,
                List.of(runningScore.runningKey()),
                runningScore.domainKey(),
                String.valueOf(currentTime)
        );

    }

}
