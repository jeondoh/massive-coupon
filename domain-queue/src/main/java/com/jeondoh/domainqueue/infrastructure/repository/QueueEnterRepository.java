package com.jeondoh.domainqueue.infrastructure.repository;

import com.jeondoh.domainqueue.api.dto.QueueDomainKey;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;
import com.jeondoh.domainqueue.domain.model.QueueEntry;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueEnterRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<List> enterQueue;

    // 대기열 진입
    // - Waiting Queue에 사용자 추가
    // - 상태, 대기 순번 및 전체 대기자 수 반환
    public QueueEntry enterQueue(QueueDomainKey queueDomainKey) {
        String waitingKey = QueueType.WAITING.getKey(queueDomainKey.domainType(), queueDomainKey.resourceId());
        String runningKey = QueueType.RUNNING.getKey(queueDomainKey.domainType(), queueDomainKey.resourceId());
        long currentTime = System.currentTimeMillis();

        List<Long> execute = redisTemplate.execute(
                enterQueue,
                List.of(waitingKey, runningKey),
                queueDomainKey.domainKey(),
                String.valueOf(currentTime)
        );

        QueueWaitOrder queueWaitOrder = QueueWaitOrder.of(execute.get(0), execute.get(1), execute.get(2));
        return QueueEntry.from(queueDomainKey, queueWaitOrder);
    }

}
