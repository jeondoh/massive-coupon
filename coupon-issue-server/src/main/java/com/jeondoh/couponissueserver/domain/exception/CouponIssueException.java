package com.jeondoh.couponissueserver.domain.exception;

import com.jeondoh.core.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CouponIssueException extends BaseException {
    public CouponIssueException(String code, String message, HttpStatusCode statusCode, String... detailMessage) {
        super(code, message, statusCode, detailMessage);
    }

    public static CouponIssueException duplicateCouponException(String... detailMessage) {
        return new CouponIssueException("Q_DUP", "쿠폰 발급은 중복될 수 없습니다.", HttpStatus.BAD_REQUEST, detailMessage);
    }

    public static CouponIssueException notFoundCouponException(String... detailMessage) {
        return new CouponIssueException("Q_NOT_FOUND", "해당 쿠폰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, detailMessage);
    }
}
