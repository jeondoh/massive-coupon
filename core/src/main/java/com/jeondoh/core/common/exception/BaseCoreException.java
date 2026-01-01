package com.jeondoh.core.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BaseCoreException extends BaseException {
    public BaseCoreException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static BaseCoreException luaFileIoException() {
        return new BaseCoreException("BASE_01", "LUA IO 파일 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static BaseCoreException amqpProduceException() {
        return new BaseCoreException("BASE_02", "MQ Produce 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static BaseCoreException amqpConsumeException() {
        return new BaseCoreException("BASE_03", "MQ Consume 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
