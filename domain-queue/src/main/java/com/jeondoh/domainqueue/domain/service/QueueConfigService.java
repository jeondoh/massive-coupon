package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueConfigDeleteRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigSaveRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigUpdateRequest;

public interface QueueConfigService {

    // 대기열 설정 영구저장
    void saveConfig(QueueConfigSaveRequest request);

    // 대기열 설정 업데이트
    void updateConfigField(QueueConfigUpdateRequest request);

    // 대기열 설정 삭제
    void deleteConfig(QueueConfigDeleteRequest request);
}
