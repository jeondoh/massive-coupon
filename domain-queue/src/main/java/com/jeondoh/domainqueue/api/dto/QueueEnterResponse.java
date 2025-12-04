package com.jeondoh.domainqueue.api.dto;

public record QueueEnterResponse(
        long status,
        long rank,
        long total
) {
    public static QueueEnterResponse from(QueueWaitOrder queueWaitOrder) {
        return new QueueEnterResponse(queueWaitOrder.status(), queueWaitOrder.rank(), queueWaitOrder.total());
    }
}
