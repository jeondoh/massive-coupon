package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.domainqueue.api.dto.QueueHeartbeat;
import com.jeondoh.domainqueue.api.dto.QueueRunningScore;
import com.jeondoh.domainqueue.domain.exception.QueueRunningScoreException;
import com.jeondoh.domainqueue.domain.repository.QueueRunningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueRunningServiceImpl implements QueueRunningService {

    private final QueueRunningRepository queueRunningRepository;

    @Override
    public void heartbeat(QueueHeartbeat queueHeartbeat) {
        QueueRunningScore queueRunningScore = QueueRunningScore.from(queueHeartbeat);
        Long result = queueRunningRepository.updateRunningScore(queueRunningScore);
        if (result == 0L) {
            throw QueueRunningScoreException.notMemberFound();
        }
    }
}
