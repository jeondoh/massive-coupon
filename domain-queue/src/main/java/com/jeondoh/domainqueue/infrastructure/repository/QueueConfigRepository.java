package com.jeondoh.domainqueue.infrastructure.repository;

import com.jeondoh.queuecore.component.QueueConfigMap;
import com.jeondoh.queuecore.domain.DomainType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_KEY;

@Repository
@RequiredArgsConstructor
public class QueueConfigRepository {

    private final StringRedisTemplate stringRedisTemplate;
    private final QueueConfigMap queueConfigMap;

    @PostConstruct
    public void init() {
        getAllConfig();
    }

    // 대기열 설정 저장
    public void saveConfig(DomainType domain, String resourceId, Map<String, String> config) {
        String key = domain.getConfigKey(resourceId);
        stringRedisTemplate.opsForHash().putAll(key, config);
        queueConfigMap.save(key, config);
    }

    // 대기열 설정 필드 저장
    public void saveConfigField(DomainType domain, String resourceId, String field, String value) {
        String key = domain.getConfigKey(resourceId);
        stringRedisTemplate.opsForHash().put(key, field, value);
        queueConfigMap.saveField(key, field, value);
    }

    // 대기열 설정 삭제
    public void deleteConfig(DomainType domain, String resourceId) {
        String key = domain.getConfigKey(resourceId);
        stringRedisTemplate.delete(key);
        queueConfigMap.delete(key);
    }

    // 모든 config 키 가져오기
    private void getAllConfig() {
        // key 전체 조회
        try (Cursor<String> cursor = stringRedisTemplate.scan(
                ScanOptions.scanOptions()
                        .match(CONFIG_KEY + ":*")
                        .count(100)
                        .build()
        )) {
            // Map<Object,Object> -> Map<String,String> 변환
            while (cursor.hasNext()) {
                String key = cursor.next();
                Map<Object, Object> rawMap = stringRedisTemplate.opsForHash().entries(key);
                Map<String, String> convertMap = new HashMap<>();
                rawMap.forEach((field, value) -> {
                    convertMap.put(field.toString(), value == null ? null : value.toString());
                });
                queueConfigMap.save(key, convertMap);
            }
        }
    }
}
