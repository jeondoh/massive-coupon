package com.jeondoh.domainqueue.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueTransferRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<List> transferJob;

    // Waiting → Running 큐 이동
    // - transferSize, threshold 기반 이동
    public void transferWaitingToRunningQueue(
            String waitingKey,
            String runningKey,
            int transferSize,
            int threshold
    ) {
        long currentTime = System.currentTimeMillis();
        redisTemplate.execute(
                transferJob,
                List.of(waitingKey, runningKey),
                String.valueOf(transferSize),
                String.valueOf(threshold),
                String.valueOf(currentTime)
        );
    }
}
