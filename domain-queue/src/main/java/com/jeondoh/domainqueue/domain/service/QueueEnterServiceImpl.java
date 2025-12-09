package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueDomainKey;
import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.domain.model.QueueEntry;
import com.jeondoh.domainqueue.infrastructure.repository.QueueEnterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueEnterServiceImpl implements QueueEnterService {

    private final QueueEnterRepository queueEnterRepository;

    // 대기열 진입
    @Override
    public QueueEntry enterQueue(QueueEnterRequest request, String memberId) {
        QueueDomainKey queueDomainKey = QueueDomainKey.of(request, memberId);

        QueueEntry queueEntry = queueEnterRepository.enterQueue(queueDomainKey);
        queueEntry.validateAlreadyInQueue();

        return queueEntry;
    }
}
