package com.jeondoh.domainqueue.infrastructure.repository;

import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueWaitOrderRepository {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<List> waitOrder;

    // 멤버의 대기순번, 대기자 수 가져오기
    // - 대기 순번 및 전체 대기자 수 반환
    public QueueWaitOrder waitOrder(String waitingKey, String domainKey) {
        List<Long> execute = stringRedisTemplate.execute(
                waitOrder,
                List.of(waitingKey),
                domainKey
        );

        return QueueWaitOrder.of(execute.get(0), execute.get(1), execute.get(2));
    }
}
