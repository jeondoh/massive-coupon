package com.jeondoh.domainqueue.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.jeondoh.core.common.util.StaticVariables.CONFIG_KEY;

@Repository
@RequiredArgsConstructor
public class QueueConfigRepository {

    private final StringRedisTemplate stringRedisTemplate;

    // 모든 config 가져오기
    public Map<String, Map<String, String>> getAllConfig() {
        Map<String, Map<String, String>> allConfigs = new HashMap<>();
        try (Cursor<String> cursor = stringRedisTemplate.scan(
                ScanOptions.scanOptions()
                        .match(CONFIG_KEY + ":*")
                        .count(100)
                        .build()
        )) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                Map<Object, Object> rawMap = stringRedisTemplate.opsForHash().entries(key);
                Map<String, String> convertMap = new HashMap<>();
                rawMap.forEach((field, value) -> {
                    convertMap.put(field.toString(), value == null ? null : value.toString());
                });
                allConfigs.put(key, convertMap);
            }
        }
        return allConfigs;
    }
}
