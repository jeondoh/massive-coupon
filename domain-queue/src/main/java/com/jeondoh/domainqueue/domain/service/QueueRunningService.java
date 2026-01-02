package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.core.common.dto.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.domainqueue.api.dto.QueueHeartbeat;

public interface QueueRunningService {

    // 하트비트로 ttl 업데이트
    void heartbeat(QueueHeartbeat queueHeartbeat);

    // running queue 멤버 제거
    void removeRunningQueueMember(CouponIssuedRemoveAtRunningQueueMessage message);
}
