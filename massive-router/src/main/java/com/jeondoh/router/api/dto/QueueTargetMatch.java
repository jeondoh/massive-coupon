package com.jeondoh.router.api.dto;

public record QueueTargetMatch(String domain, String resourceId) {
    public static QueueTargetMatch of(String domain, String resourceId) {
        return new QueueTargetMatch(domain, resourceId);
    }
}
