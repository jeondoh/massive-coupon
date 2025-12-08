package com.jeondoh.domainqueue.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueExtractMemberScheduler {

    // running queue 멤버 제거
    // 조건
    // - 1. 마지막 응답(score) 시간이 n초 이상 차이나는 경우
    // - 2. 입장 후 n분 이상 지났을 경우
    @Scheduled(fixedDelayString = "${queue.transfer.interval}")
    public void extractMember() {

    }
}
