package com.jeondoh.domainqueue.domain.service;

import com.jeondoh.core.common.dto.coupon.CouponIssuedRemoveAtRunningQueueMessage;
import com.jeondoh.domainqueue.api.dto.QueueHeartbeat;
import com.jeondoh.domainqueue.api.dto.QueueRunningScore;
import com.jeondoh.domainqueue.api.dto.RemoveRunningQueueMember;
import com.jeondoh.domainqueue.domain.exception.QueueRunningScoreException;
import com.jeondoh.domainqueue.infrastructure.repository.QueueRunningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueRunningServiceImpl implements QueueRunningService {

    private final QueueRunningRepository queueRunningRepository;

    // 하트비트로 ttl 업데이트
    @Override
    public void heartbeat(QueueHeartbeat queueHeartbeat) {
        QueueRunningScore queueRunningScore = QueueRunningScore.from(queueHeartbeat);
        Long result = queueRunningRepository.updateRunningScore(queueRunningScore);
        if (result == 0L) {
            throw QueueRunningScoreException.notMemberFound();
        }
    }

    // running queue 멤버 제거
    @Override
    public void removeRunningQueueMember(CouponIssuedRemoveAtRunningQueueMessage message) {
        RemoveRunningQueueMember removeRunningQueueMember = RemoveRunningQueueMember.from(message);
        queueRunningRepository.removeMember(removeRunningQueueMember);
    }
}
