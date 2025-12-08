package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueDomainMember;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrderResponse;
import com.jeondoh.domainqueue.domain.exception.QueueWaitOrderException;
import com.jeondoh.domainqueue.domain.repository.QueueWaitOrderRepository;
import com.jeondoh.queuecore.domain.DomainType;
import com.jeondoh.queuecore.domain.QueueType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueWaitOrderServiceImpl implements QueueWaitOrderService {

    private final QueueWaitOrderRepository queueWaitOrderRepository;

    // 대기 순번
    // 반환: 대기 순번, 전체 대기자 수
    @Override
    public QueueWaitOrderResponse waitOrder(QueueDomainMember queueDomainMember) {
        DomainType domain = queueDomainMember.domain();
        String resourceId = queueDomainMember.resourceId();
        String memberId = queueDomainMember.memberId();
        String waitingKey = QueueType.WAITING.getKey(domain, resourceId);

        QueueWaitOrder queueWaitOrder = queueWaitOrderRepository.waitOrder(
                waitingKey,
                domain.getDomainKey(resourceId, memberId)
        );

        if (queueWaitOrder.status() == 0L) {
            throw QueueWaitOrderException.noMemberInWaitingQueue();
        }

        return QueueWaitOrderResponse.from(queueWaitOrder);
    }
}
