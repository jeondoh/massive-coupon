package com.jeondoh.router.domain.service;

import com.jeondoh.queuecore.component.QueueConfigMap;
import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.exception.QueueConfigException;
import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.api.dto.QueueRunningMemberCheck;
import com.jeondoh.router.infrastructure.repository.QueueLuaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueConfigMap queueConfigMap;
    private final QueueLuaRepository queueLuaRepository;

    // Config 존재 여부 확인
    // - 없다면 default config 에서 복제
    // - default config 마저 없다면 오류
    public Mono<Void> createConfigIfNotExists(QueueConfigExists queueConfigExists) {
        DomainType domain = queueConfigExists.domainType();
        String resourceId = queueConfigExists.resourceId();

        return queueLuaRepository.createConfigIfNotExists(queueConfigExists)
                .next()
                .switchIfEmpty(Mono.just(-1L))
                .flatMap(result -> {
                    if (result == 1L) {
                        // 새로운 Config 생성
                        queueConfigMap.saveConfigFromDuplicateDefaultConfig(domain, resourceId);
                    } else if (result == -1L) {
                        // default Config 필드 누락
                        log.error("Config 필드 누락: {}:{}", domain.name(), resourceId);
                        return Mono.error(QueueConfigException.notFoundConfigException(domain.name(), resourceId));
                    }
                    return Mono.empty();
                });
    }

    // 대기열 여부 체크
    // - 트래픽 카운트 증가
    // - Waiting, Running Queue 크기 기반 체크
    // - RPM 기반 체크
    @Override
    public Mono<Boolean> checkQueueRequired(QueueConfigExists queueConfigExists) {
        return queueLuaRepository.checkQueueRequired(queueConfigExists)
                .next()
                .flatMap(result -> {
                    if (result == 1L) {
                        return Mono.just(true);
                    }
                    return Mono.just(false);
                });
    }

    // Running Queue 멤버 존재 여부 확인
    @Override
    public Mono<Boolean> checkMemberInRunningQueue(QueueRunningMemberCheck queueRunningMemberCheck) {
        return queueLuaRepository.checkMemberInRunningQueue(queueRunningMemberCheck);
    }
}
