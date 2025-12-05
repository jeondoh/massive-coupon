package com.jeondoh.router.api.dto;

public record QueueWaitOrderResponse(
        long status,
        long rank,
        long total
) {
    public static QueueWaitOrderResponse of(Object status, Object rank, Object total) {
        long statusLong = ((Number) status).longValue();
        long rankLong = ((Number) rank).longValue();
        long totalLong = ((Number) total).longValue();
        return new QueueWaitOrderResponse(statusLong, rankLong, totalLong);
    }
}
