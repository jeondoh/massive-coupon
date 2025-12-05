package com.jeondoh.router.domain.service;

import com.jeondoh.router.api.dto.QueueWaitOrder;
import com.jeondoh.router.api.dto.QueueWaitOrderResponse;
import reactor.core.publisher.Mono;

public interface QueueWaitOrderService {

    // 대기 순번
    // 반환: 대기 순번, 전체 대기자 수
    Mono<QueueWaitOrderResponse> waitOrder(QueueWaitOrder queueWaitOrder);

}
