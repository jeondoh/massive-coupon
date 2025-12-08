package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueDomainMember;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrderResponse;

public interface QueueWaitOrderService {

    // 대기 순번
    // 반환: 대기 순번, 전체 대기자 수
    QueueWaitOrderResponse waitOrder(QueueDomainMember queueDomainMember);

}
