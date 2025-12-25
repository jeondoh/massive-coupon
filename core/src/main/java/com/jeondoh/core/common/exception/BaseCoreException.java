package com.jeondoh.core.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BaseCoreException extends BaseException {
    public BaseCoreException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static BaseCoreException luaFileIoException() {
        return new BaseCoreException("CORE_LUA_IO", "LUA IO 파일 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
