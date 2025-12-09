package com.jeondoh.router.infrastructure.repository;

import com.jeondoh.core.common.dto.DomainType;
import com.jeondoh.queuecore.domain.QueueType;
import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.api.dto.QueueRunningMemberCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_THRESHOLD;
import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_TRAFFIC_RPM;

@Repository
public class QueueLuaRepository {

    public QueueLuaRepository(
            @Value("${queue.traffic.ttl-seconds}") long trafficTTLSeconds,
            RedisScript<Long> createConfigIfNotExists,
            RedisScript<Long> checkQueueRequired,
            ReactiveRedisTemplate<String, String> reactiveRedisTemplate
    ) {
        this.trafficTTLSeconds = trafficTTLSeconds;
        this.createConfigIfNotExists = createConfigIfNotExists;
        this.checkQueueRequired = checkQueueRequired;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    private final long trafficTTLSeconds;
    private final RedisScript<Long> createConfigIfNotExists;
    private final RedisScript<Long> checkQueueRequired;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    // Config 존재 여부 확인
    // - 없다면 default config 에서 복제
    // - default config 마저 없다면 오류
    public Flux<Long> createConfigIfNotExists(QueueConfigExists queueConfigExists) {
        DomainType domainType = queueConfigExists.domainType();
        String resourceId = queueConfigExists.resourceId();

        List<String> keys = new ArrayList<>();
        keys.add(DomainType.getDefaultConfigKey());
        keys.add(domainType.getConfigKey(resourceId));

        return reactiveRedisTemplate.execute(
                createConfigIfNotExists,
                keys
        );
    }

    // 대기열 여부 체크
    // - 트래픽 카운트 증가
    // - Waiting, Running Queue 크기 기반 체크
    // - RPM 기반 체크
    public Flux<Long> checkQueueRequired(QueueConfigExists queueConfigExists) {
        DomainType domain = queueConfigExists.domainType();
        String resourceId = queueConfigExists.resourceId();
        LocalDateTime now = LocalDateTime.now();

        List<String> keys = new ArrayList<>(12);
        // KEYS[1]: traffic 키
        String trafficKey = domain.getTrafficKey(resourceId, now);
        keys.add(trafficKey);
        // KEYS[2]: Waiting Queue 키
        keys.add(QueueType.WAITING.getKey(domain, resourceId));
        // KEYS[3]: Running Queue 키
        keys.add(QueueType.RUNNING.getKey(domain, resourceId));
        // KEYS[4]: Config 키
        keys.add(domain.getConfigKey(resourceId));
        // KEYS[5~10]: 최근 6개 Traffic 버킷 키 (RPM)
        // - 10초단위 버킷 6개
        for (int i = 0; i < 6; i++) {
            LocalDateTime bucketTime = now.minusSeconds(i * 10L);
            keys.add(domain.getTrafficKey(resourceId, bucketTime));
        }

        return reactiveRedisTemplate.execute(
                checkQueueRequired,
                keys,
                CONFIG_THRESHOLD,
                CONFIG_TRAFFIC_RPM,
                String.valueOf(trafficTTLSeconds)
        );
    }

    // Running Queue 멤버 존재 여부 확인
    public Mono<Boolean> checkMemberInRunningQueue(QueueRunningMemberCheck queueRunningMemberCheck) {
        DomainType domain = queueRunningMemberCheck.domain();
        String resourceId = queueRunningMemberCheck.resourceId();
        String runningKey = QueueType.RUNNING.getKey(domain, resourceId);
        String domainKey = domain.getDomainKey(resourceId, queueRunningMemberCheck.memberId());

        return reactiveRedisTemplate.opsForZSet()
                .score(runningKey, domainKey)
                .hasElement();
    }

}
