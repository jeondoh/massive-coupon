package com.jeondoh.core.common.exception;

import org.springframework.http.HttpStatus;

public class JwtDecodedException extends BaseException {
    
    public JwtDecodedException(String code, String message, String... detailMessage) {
        super(code, message, HttpStatus.UNAUTHORIZED, detailMessage);
    }

    public static JwtDecodedException invalidSignedException() {
        return new JwtDecodedException("JWT_01", "잘못된 Jwt 서명입니다.");
    }

    public static JwtDecodedException expiredException() {
        return new JwtDecodedException("JWT_02", "만료된 Jwt 토큰입니다.");
    }

    public static JwtDecodedException unsupportedException() {
        return new JwtDecodedException("JWT_03", "지원되지 않는 JWT 토큰입니다.");
    }

    public static JwtDecodedException illegalArgumentException() {
        return new JwtDecodedException("JWT_04", "JWT 토큰이 잘못되었습니다.");
    }
}
