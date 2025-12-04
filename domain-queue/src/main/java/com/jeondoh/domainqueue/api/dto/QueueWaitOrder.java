package com.jeondoh.domainqueue.api.dto;

public record QueueWaitOrder(
        long status,
        long rank,
        long total
) {
    public static QueueWaitOrder of(long status, long rank, long total) {
        return new QueueWaitOrder(status, rank, total);
    }

}
