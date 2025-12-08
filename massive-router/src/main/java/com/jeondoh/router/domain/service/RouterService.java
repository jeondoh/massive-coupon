package com.jeondoh.router.domain.service;

import com.jeondoh.router.api.dto.QueueTargetMatch;
import reactor.core.publisher.Mono;

public interface RouterService {

    // 대기열 필요 여부
    Mono<Boolean> checkQueueRequired(QueueTargetMatch queueTargetMatch);
}
