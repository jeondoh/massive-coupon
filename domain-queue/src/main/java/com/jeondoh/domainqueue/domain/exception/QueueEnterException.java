package com.jeondoh.domainqueue.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class QueueEnterException extends BaseException {
    public QueueEnterException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static QueueEnterException alreadyWaitingException() {
        return new QueueEnterException("Q_ENTER_01", "이미 대기중인 상태입니다.", HttpStatus.CONFLICT);
    }

    public static QueueEnterException alreadyRunningException() {
        return new QueueEnterException("Q_ENTER_02", "이미 참여중인 상태입니다.", HttpStatus.CONFLICT);
    }
}
