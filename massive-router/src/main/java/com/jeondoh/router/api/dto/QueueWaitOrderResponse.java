package com.jeondoh.router.api.dto;

public record QueueWaitOrderResponse(
        long status,
        long rank,
        long total
) {
    public static QueueWaitOrderResponse of(long status, Object rank, Object total) {
        long rankLong = ((Number) rank).longValue();
        long totalLong = ((Number) total).longValue();
        return new QueueWaitOrderResponse(status, rankLong, totalLong);
    }
}
