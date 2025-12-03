package com.jeondoh.queuecore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeHelper {
    private static final DateTimeFormatter BUCKET_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 10초 단위 버킷
    // - 트래픽 측정시 사용
    public static String formatToTenSecondBucket(LocalDateTime dateTime) {
        int second = dateTime.getSecond();
        int bucketSecond = (second / 10) * 10;

        LocalDateTime bucketTime = dateTime
                .withSecond(bucketSecond)
                .withNano(0);

        return bucketTime.format(BUCKET_FORMATTER);
    }
}
