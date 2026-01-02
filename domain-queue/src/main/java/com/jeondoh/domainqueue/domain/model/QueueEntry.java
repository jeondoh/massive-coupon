package com.jeondoh.domainqueue.domain.model;

import com.jeondoh.core.common.dto.QueueType;
import com.jeondoh.domainqueue.api.dto.QueueDomainKey;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;
import com.jeondoh.domainqueue.domain.exception.QueueEnterException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueueEntry {
    private QueueDomainKey queueDomainKey;
    private QueueWaitOrder queueWaitOrder;

    // 중복 참여 여부 확인
    public void validateAlreadyInQueue() {
        // 이미 참여중
        if (queueWaitOrder.status() == -1) {
            String runningKey = QueueType.RUNNING.getKey(queueDomainKey.domainType(), queueDomainKey.resourceId());
            log.warn("이미 참여중입니다. {}, {}", runningKey, queueDomainKey.resourceId());
            throw QueueEnterException.alreadyRunningException();
        } else if (queueWaitOrder.status() == 0) {
            // 이미 대기중
            String waitingKey = QueueType.WAITING.getKey(queueDomainKey.domainType(), queueDomainKey.resourceId());
            log.warn("이미 대기중입니다. {}, {}", waitingKey, queueDomainKey.resourceId());
            throw QueueEnterException.alreadyWaitingException();
        }
    }

    public static QueueEntry from(QueueDomainKey queueDomainKey, QueueWaitOrder queueWaitOrder) {
        return QueueEntry.builder()
                .queueDomainKey(queueDomainKey)
                .queueWaitOrder(queueWaitOrder)
                .build();
    }

}
