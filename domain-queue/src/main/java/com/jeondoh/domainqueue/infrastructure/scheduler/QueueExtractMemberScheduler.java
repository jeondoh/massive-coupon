package com.jeondoh.domainqueue.infrastructure.scheduler;

import com.jeondoh.domainqueue.infrastructure.repository.QueueExtractRepository;
import com.jeondoh.queuecore.component.QueueConfigMap;
import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueExtractMemberScheduler {

    @Value("${queue.extract.timeout}")
    private long timeout;
    @Value("${queue.token.key-prefix}")
    private String tokenKeyPrefix;
    private final QueueConfigMap queueConfigMap;
    private final QueueExtractRepository queueExtractRepository;

    // running queue 멤버 제거
    // - 마지막 응답 heartbeat 시간(score)이 timeout밀리초 이상 차이나는 경우
    // - 토큰도 함께 삭제
    @Scheduled(fixedDelayString = "${queue.extract.interval}")
    public void extractMember() {
        // domain에 해당하는 config key 조회
        Set<String> configKeys = queueConfigMap.getAllConfigKeys();
        for (String configKey : configKeys) {
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
                timeoutScore,
                tokenKeyPrefix
        );
    }
}
