package com.jeondoh.router.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatusCode;

public class ForwardingException extends BaseException {

    public ForwardingException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static ForwardingException forwardApiServerException(
            String code,
            String message,
            HttpStatusCode statusCode,
            String... detailMessage
    ) {
        return new ForwardingException(code, message, statusCode, detailMessage);
    }

}
