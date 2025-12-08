package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueHeartbeat;

public interface QueueRunningService {

    void heartbeat(QueueHeartbeat queueHeartbeat);
}
