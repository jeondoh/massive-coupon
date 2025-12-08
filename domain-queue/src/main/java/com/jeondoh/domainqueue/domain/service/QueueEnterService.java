package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.domain.model.QueueEntry;

public interface QueueEnterService {

    // 대기열 진입
    QueueEntry enterQueue(QueueEnterRequest request, String memberId);
}
