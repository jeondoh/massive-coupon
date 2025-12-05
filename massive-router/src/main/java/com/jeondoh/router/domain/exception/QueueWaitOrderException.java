package com.jeondoh.router.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class QueueWaitOrderException extends BaseException {

    public QueueWaitOrderException(String code, String message, HttpStatusCode statusCode, String redirectUrl) {
        super(code, message, statusCode, redirectUrl);
    }

    public QueueWaitOrderException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static QueueWaitOrderException noMemberInWaitingQueue() {
        return new QueueWaitOrderException("Q_WAIT_ORDER_01", "대기열에 멤버가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
