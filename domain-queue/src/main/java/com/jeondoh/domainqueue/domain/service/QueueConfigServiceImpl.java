package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.core.common.exception.QueueConfigException;
import com.jeondoh.domainqueue.api.dto.QueueConfigDeleteRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigRequest;
import com.jeondoh.domainqueue.domain.model.QueueConfigHash;
import com.jeondoh.domainqueue.infrastructure.repository.QueueConfigCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueConfigServiceImpl implements QueueConfigService {

    private final QueueConfigCrudRepository queueConfigCrudRepository;

    // 대기열 설정 영구저장
    @Override
    public void saveConfig(QueueConfigRequest request) {
        QueueConfigHash configHash = QueueConfigHash.from(request);
        queueConfigCrudRepository.save(configHash);
    }

    // 대기열 설정 업데이트
    @Override
    public void updateConfigField(QueueConfigRequest request) {
        String id = QueueConfigHash.createId(request.domain(), request.resourceId());
        QueueConfigHash queueConfigHash =
                queueConfigCrudRepository.findById(id)
                        .orElseThrow(QueueConfigException::notFoundConfigException);

        queueConfigHash.updateFieldValue(request);
        queueConfigCrudRepository.save(queueConfigHash);
    }

    // 대기열 설정 삭제
    @Override
    public void deleteConfig(QueueConfigDeleteRequest request) {
        String id = QueueConfigHash.createId(request.domain(), request.resourceId());

        queueConfigCrudRepository.deleteById(id);
    }

}
