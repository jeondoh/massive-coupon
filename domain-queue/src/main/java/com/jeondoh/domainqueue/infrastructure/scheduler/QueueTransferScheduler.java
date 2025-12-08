package com.jeondoh.domainqueue.infrastructure.scheduler;

import com.jeondoh.domainqueue.domain.repository.QueueTransferRepository;
import com.jeondoh.queuecore.component.QueueConfigMap;
import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_THRESHOLD;
import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_TRANSFER_SIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueTransferScheduler {

    private final QueueConfigMap queueConfigMap;
    private final QueueTransferRepository queueTransferRepository;

    // waiting queue -> running queue 이동
    @Scheduled(fixedDelayString = "${queue.transfer.interval}")
    public void transferQueue() {
        // domain 에 해당하는 config key 조회
        Set<String> configKeys = queueConfigMap.getAllConfigKeys();
        for (String configKey : configKeys) {
            try {
                processQueue(configKey);
            } catch (Exception e) {
                log.error("큐 처리 중 오류 발생: configKey={}, error={}", configKey, e.getMessage(), e);
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

        // Config 조회
        int threshold = Integer.parseInt(queueConfigMap.get(configKey, CONFIG_THRESHOLD));
        int transferSize = Integer.parseInt(queueConfigMap.get(configKey, CONFIG_TRANSFER_SIZE));

        // key 가져오기
        DomainType domainType = DomainType.valueOf(domainName);
        String waitingKey = QueueType.WAITING.getKey(domainType, resourceId);
        String runningKey = QueueType.RUNNING.getKey(domainType, resourceId);

        // Waiting → Running 큐 이동
        queueTransferRepository.transferWaitingToRunningQueue(
                waitingKey,
                runningKey,
                transferSize,
                threshold
        );
    }

}
