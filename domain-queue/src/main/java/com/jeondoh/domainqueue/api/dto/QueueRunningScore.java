package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.core.common.dto.QueueType;

public record QueueRunningScore(
        String runningKey,
        String domainKey
) {
    public static QueueRunningScore from(QueueHeartbeat queueHeartbeat) {
        String resourceId = queueHeartbeat.resourceId();
        String memberId = queueHeartbeat.memberId();
        String runningKey = QueueType.RUNNING.getKey(queueHeartbeat.domainType(), queueHeartbeat.resourceId());
        String domainKey = queueHeartbeat.domainType().getDomainKey(resourceId, memberId);
        return new QueueRunningScore(runningKey, domainKey);
    }
}
