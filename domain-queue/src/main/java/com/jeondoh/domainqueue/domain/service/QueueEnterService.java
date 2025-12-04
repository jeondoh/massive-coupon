package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;

public interface QueueEnterService {

    // 대기열 진입
    QueueWaitOrder enterQueue(QueueEnterRequest request, String memberId);
}
