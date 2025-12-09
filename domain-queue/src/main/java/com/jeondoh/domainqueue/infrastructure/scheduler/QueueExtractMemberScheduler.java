package com.jeondoh.domainqueue.infrastructure.scheduler;

import com.jeondoh.core.common.dto.DomainType;
import com.jeondoh.domainqueue.infrastructure.repository.QueueConfigRepository;
import com.jeondoh.domainqueue.infrastructure.repository.QueueExtractRepository;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueExtractMemberScheduler {

    @Value("${queue.extract.timeout}")
    private long timeout;
    private final QueueExtractRepository queueExtractRepository;
    private final QueueConfigRepository queueConfigRepository;

    // running queue 멤버 제거
    // - 마지막 응답 heartbeat 시간(score)이 timeout밀리초 이상 차이나는 경우
    @Scheduled(fixedDelayString = "${queue.extract.interval}")
    public void extractMember() {
        // 모든 config 조회
        Map<String, Map<String, String>> allConfigs = queueConfigRepository.getAllConfig();
        for (String configKey : allConfigs.keySet()) {
            try {
                processQueue(configKey);
            } catch (Exception e) {
                log.error("큐 멤버 추출 중 오류 발생: configKey={}, error={}", configKey, e.getMessage(), e);
            }
        }
    }

    // 큐 처리
    private void processQueue(String configKey) {
        String[] parts = configKey.split(":");
        if (parts.length != 3) {
            return;
        }

        String domainName = parts[1];
        String resourceId = parts[2];

        // key 가져오기
        DomainType domainType = DomainType.valueOf(domainName);
        String runningKey = QueueType.RUNNING.getKey(domainType, resourceId);
        long currentTime = System.currentTimeMillis();
        long timeoutScore = currentTime - timeout;

        // timeout된 멤버 제거
        queueExtractRepository.extractTimeoutMembers(
                runningKey,
                timeoutScore
        );
    }
}
