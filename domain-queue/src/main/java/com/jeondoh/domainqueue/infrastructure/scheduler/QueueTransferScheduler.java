package com.jeondoh.domainqueue.infrastructure.scheduler;

import com.jeondoh.core.common.dto.DomainType;
import com.jeondoh.domainqueue.infrastructure.repository.QueueConfigRepository;
import com.jeondoh.domainqueue.infrastructure.repository.QueueTransferRepository;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_THRESHOLD;
import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_TRANSFER_SIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueTransferScheduler {

    private final QueueConfigRepository queueConfigRepository;
    private final QueueTransferRepository queueTransferRepository;

    // waiting queue -> running queue 이동
    @Scheduled(fixedDelayString = "${queue.transfer.interval}")
    public void transferQueue() {
        // 모든 config 조회
        Map<String, Map<String, String>> allConfigs = queueConfigRepository.getAllConfig();
        for (String configKey : allConfigs.keySet()) {
            try {
                processQueue(configKey, allConfigs.get(configKey));
            } catch (Exception e) {
                log.error("큐 처리 중 오류 발생: configKey={}, error={}", configKey, e.getMessage(), e);
            }
        }
    }

    // 큐 처리
    private void processQueue(String configKey, Map<String, String> config) {
        String[] parts = configKey.split(":");
        if (parts.length != 3) {
            return;
        }

        String domainName = parts[1];
        String resourceId = parts[2];

        // Config 조회
        int threshold = Integer.parseInt(config.get(CONFIG_THRESHOLD));
        int transferSize = Integer.parseInt(config.get(CONFIG_TRANSFER_SIZE));

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
