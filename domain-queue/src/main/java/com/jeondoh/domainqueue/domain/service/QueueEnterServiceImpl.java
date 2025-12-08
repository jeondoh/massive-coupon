package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueDomainKey;
import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.domain.model.QueueEntry;
import com.jeondoh.domainqueue.infrastructure.repository.QueueEnterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueEnterServiceImpl implements QueueEnterService {

    public QueueEnterServiceImpl(
            QueueEnterRepository queueEnterRepository,
            @Value("${queue.token.key-prefix}") String keyPrefix,
            @Value("${queue.token.ttl-seconds}") int tokenTTL
    ) {
        this.queueEnterRepository = queueEnterRepository;
        this.keyPrefix = keyPrefix;
        this.tokenTTL = tokenTTL;
    }

    private final QueueEnterRepository queueEnterRepository;
    private final String keyPrefix;
    private final int tokenTTL;

    // 대기열 진입
    @Override
    public QueueEntry enterQueue(QueueEnterRequest request, String memberId) {
        QueueDomainKey queueDomainKey = QueueDomainKey.of(request, memberId, keyPrefix, tokenTTL);

        QueueEntry queueEntry = queueEnterRepository.enterQueue(queueDomainKey);
        queueEntry.validateAlreadyInQueue();

        return queueEntry;
    }
}
