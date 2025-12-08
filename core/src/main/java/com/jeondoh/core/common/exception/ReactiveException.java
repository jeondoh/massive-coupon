package com.jeondoh.core.common.exception;

import org.springframework.http.HttpStatusCode;

public class ReactiveException extends BaseException {

    public ReactiveException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static ReactiveException reactiveException(HttpStatusCode statusCode) {
        return new ReactiveException("INTERNAL_SERVER_ERROR", "INTERNAL_SERVER_ERROR", statusCode);
    }
}
