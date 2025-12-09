package com.jeondoh.router.domain.service;

import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.api.dto.QueueRunningMemberCheck;
import reactor.core.publisher.Mono;

public interface QueueService {

    // Config 존재 여부 확인
    Mono<Void> createConfigIfNotExists(QueueConfigExists queueConfigExists);

    // 대기열 여부 체크
    // - 트래픽 카운트 증가
    // - Waiting, Running Queue 크기 기반 체크
    // - RPM 기반 체크
    Mono<Boolean> checkQueueRequired(QueueConfigExists queueConfigExists);

    // Running Queue 멤버 존재 여부 확인
    Mono<Boolean> checkMemberInRunningQueue(QueueRunningMemberCheck queueRunningMemberCheck);
}
