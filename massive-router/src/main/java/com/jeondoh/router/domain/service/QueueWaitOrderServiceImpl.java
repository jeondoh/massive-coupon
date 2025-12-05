package com.jeondoh.router.domain.service;

import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.domain.QueueType;
import com.jeondoh.router.api.dto.QueueWaitOrder;
import com.jeondoh.router.api.dto.QueueWaitOrderResponse;
import com.jeondoh.router.domain.exception.QueueWaitOrderException;
import com.jeondoh.router.domain.repository.QueueLuaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QueueWaitOrderServiceImpl implements QueueWaitOrderService {

    private final QueueLuaRepository queueLuaRepository;

    // 대기 순번
    // 반환: 대기 순번, 전체 대기자 수
    @Override
    public Mono<QueueWaitOrderResponse> waitOrder(QueueWaitOrder queueWaitOrder) {
        DomainType domain = queueWaitOrder.domain();
        String resourceId = queueWaitOrder.resourceId();
        String memberId = queueWaitOrder.memberId();
        String waitingKey = QueueType.WAITING.getKey(domain, resourceId);

        return queueLuaRepository.waitOrder(waitingKey, domain.getDomainKey(resourceId, memberId))
                .next()
                .flatMap(result -> {
                    Object status = result.getFirst();
                    if (status == null || status.equals(0)) {
                        return Mono.error(QueueWaitOrderException.noMemberInWaitingQueue());
                    }
                    QueueWaitOrderResponse queueWaitOrderResponse = QueueWaitOrderResponse.of(
                            status,
                            result.get(1),
                            result.get(2)
                    );
                    return Mono.just(queueWaitOrderResponse);
                });
    }
}
