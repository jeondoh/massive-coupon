package com.jeondoh.router.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RouterException extends BaseException {

    public RouterException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static RouterException QueueRequiredException(String redirectUrl) {
        return new RouterException("ROUTER_01", "대기열 서버로의 라우팅이 필요합니다", HttpStatus.PERMANENT_REDIRECT, redirectUrl);
    }

    public static RouterException QueueTokenRequiredException() {
        return new RouterException("ROUTER_02", "대기열 요청시 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED);
    }
}
