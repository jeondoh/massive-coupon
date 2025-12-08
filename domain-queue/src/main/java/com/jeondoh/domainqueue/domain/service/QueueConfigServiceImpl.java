package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueConfigDeleteRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigSaveRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigUpdateRequest;
import com.jeondoh.domainqueue.infrastructure.repository.QueueConfigRepository;
import com.jeondoh.queuecore.domain.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.jeondoh.queuecore.utils.StaticVariables.*;

@Service
@RequiredArgsConstructor
public class QueueConfigServiceImpl implements QueueConfigService {

    private final QueueConfigRepository queueConfigRepository;

    // 대기열 설정 영구저장
    @Override
    public void saveConfig(QueueConfigSaveRequest request) {
        Map<String, String> config = new HashMap<>();
        config.put(CONFIG_THRESHOLD, String.valueOf(request.threshold()));
        config.put(CONFIG_TRANSFER_SIZE, String.valueOf(request.transferSize()));
        config.put(CONFIG_TRANSFER_INTERVAL, String.valueOf(request.transferInterval()));
        config.put(CONFIG_TRAFFIC_RPM, String.valueOf(request.trafficRpm()));

        queueConfigRepository.saveConfig(
                DomainType.valueOf(request.domain()),
                request.resourceId(),
                config
        );
    }

    // 대기열 설정 업데이트
    @Override
    public void updateConfigField(QueueConfigUpdateRequest request) {
        queueConfigRepository.saveConfigField(
                DomainType.valueOf(request.domain()),
                request.resourceId(),
                request.field(),
                request.value()
        );
    }

    // 대기열 설정 삭제
    @Override
    public void deleteConfig(QueueConfigDeleteRequest request) {
        queueConfigRepository.deleteConfig(
                DomainType.valueOf(request.domain()),
                request.resourceId()
        );
    }
}
