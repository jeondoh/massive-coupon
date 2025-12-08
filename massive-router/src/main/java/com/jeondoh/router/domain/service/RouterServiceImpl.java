package com.jeondoh.router.domain.service;

import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.api.dto.QueueTargetMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouterServiceImpl implements RouterService {

    private final QueueService queueService;

    // 대기열 필요 여부
    @Override
    public Mono<Boolean> checkQueueRequired(QueueTargetMatch queueTargetMatch) {
        QueueConfigExists queueConfig = QueueConfigExists.of(queueTargetMatch.domain(), queueTargetMatch.resourceId());
        return queueService.createConfigIfNotExists(queueConfig)
                .then(queueService.checkQueueRequired(queueConfig));
    }

}
