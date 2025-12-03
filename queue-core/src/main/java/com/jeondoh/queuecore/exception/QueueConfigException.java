package com.jeondoh.queuecore.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class QueueConfigException extends BaseException {
    public QueueConfigException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static QueueConfigException luaFileIoException() {
        return new QueueConfigException("Q_CONFIG_01", "LUA IO 파일 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static QueueConfigException notFoundConfigException(String... detailMessage) {
        return new QueueConfigException("Q_CONFIG_02", "설정값이 누락되었습니다.", HttpStatus.INTERNAL_SERVER_ERROR, detailMessage);
    }
}
