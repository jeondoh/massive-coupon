package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueHeartbeat(
        DomainType domainType,
        String resourceId,
        String memberId
) {
    public static QueueHeartbeat of(QueueHeartbeatRequest queueHeartbeatRequest, String memberId) {
        return new QueueHeartbeat(
                DomainType.valueOf(queueHeartbeatRequest.domain()),
                queueHeartbeatRequest.resourceId(),
                memberId
        );
    }
}
