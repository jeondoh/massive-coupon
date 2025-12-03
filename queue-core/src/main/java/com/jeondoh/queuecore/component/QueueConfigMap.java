package com.jeondoh.queuecore.component;

import com.jeondoh.queuecore.annotation.Structure;
import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.exception.QueueConfigException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Structure
public class QueueConfigMap {

    private final Map<String, Map<String, String>> queueConfigMap = new ConcurrentHashMap<>();

    // 저장
    public void save(String key, Map<String, String> value) {
        queueConfigMap.put(key, new ConcurrentHashMap<>(value));
    }

    // 필드 저장
    public void saveField(String key, String configKey, String value) {
        queueConfigMap.get(key).put(configKey, value);
    }

    // default Config 기반 신규 config 생성
    public void saveConfigFromDuplicateDefaultConfig(DomainType domain, String resourceId) {
        String domainKey = domain.getConfigKey(resourceId);
        String defaultConfigKey = DomainType.getDefaultConfigKey();

        Map<String, String> defaultConfigMap = queueConfigMap.get(defaultConfigKey);
        if (defaultConfigMap == null || defaultConfigMap.isEmpty()) {
            throw QueueConfigException.notFoundConfigException(defaultConfigKey);
        }

        queueConfigMap.put(domainKey, new ConcurrentHashMap<>(defaultConfigMap));
    }

    // 값 반환
    public String get(String key, String configKey) {
        return Optional.ofNullable(queueConfigMap.get(key))
                .orElseThrow(() -> QueueConfigException.notFoundConfigException(key))
                .get(configKey);
    }

    // 전체 key 반환
    public Set<String> getAllConfigKeys() {
        return queueConfigMap.keySet();
    }

    // 삭제
    public void delete(String key) {
        queueConfigMap.remove(key);
    }

}
