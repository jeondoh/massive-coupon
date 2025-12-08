package com.jeondoh.domainqueue.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class QueueRunningScoreException extends BaseException {
    public QueueRunningScoreException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static QueueRunningScoreException notMemberFound() {
        return new QueueRunningScoreException("Q_RUNNING_01", "running 큐에서 멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
