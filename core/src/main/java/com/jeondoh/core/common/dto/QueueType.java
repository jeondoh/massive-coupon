package com.jeondoh.core.common.dto;

import lombok.Getter;

@Getter
public enum QueueType {
    WAITING("waiting-queue"),
    RUNNING("running-queue");

    private final String queueName;

    QueueType(String queueName) {
        this.queueName = queueName;
    }

    public String getKey(DomainType domain, String resourceId) {
        return String.format("%s:%s:%s", queueName, domain.name(), resourceId);
    }
}
