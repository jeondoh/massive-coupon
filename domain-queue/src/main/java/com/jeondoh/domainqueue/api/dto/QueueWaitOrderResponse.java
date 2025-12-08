package com.jeondoh.domainqueue.api.dto;

public record QueueWaitOrderResponse(
        long status,
        long rank,
        long total
) {
    public static QueueWaitOrderResponse from(QueueWaitOrder queueWaitOrder) {
        return new QueueWaitOrderResponse(queueWaitOrder.status(), queueWaitOrder.rank(), queueWaitOrder.total());
    }

}
