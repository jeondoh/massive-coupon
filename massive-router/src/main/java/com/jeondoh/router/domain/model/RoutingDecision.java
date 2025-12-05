package com.jeondoh.router.domain.model;

import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoutingDecision {

    private final QueueService queueService;

    // 대기열 설정 확인 후 라우팅 결정
    // - 대기열 설정이 없으면 생성
    // - 대기열 필요 여부 판단
    public Mono<RoutingType> decide(QueueConfigExists queueConfig) {
        return queueService.createConfigIfNotExists(queueConfig)
                .then(queueService.checkQueueRequired(queueConfig))
                .map(needsQueue -> needsQueue
                        ? RoutingType.REDIRECT_TO_QUEUE
                        : RoutingType.FORWARD_TO_BACKEND
                );
    }
}
