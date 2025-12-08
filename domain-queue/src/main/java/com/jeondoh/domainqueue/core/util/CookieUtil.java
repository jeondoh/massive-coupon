package com.jeondoh.domainqueue.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public CookieUtil(
            @Value("${spring.profiles.active}") String activeProfile,
            @Value("${queue.token.key-prefix}") String tokenKeyPrefix,
            @Value("${queue.token.max-age}") int tokenMaxAge
    ) {
        this.tokenKeyPrefix = tokenKeyPrefix;
        this.tokenMaxAge = tokenMaxAge;
        this.isProd = activeProfile.equals("prod") || activeProfile.equals("production");
    }

    private final String tokenKeyPrefix;
    private final int tokenMaxAge;
    private final boolean isProd;

    // 쿠키 저장
    public void saveQueueTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(tokenKeyPrefix, token);
        cookie.setPath("/");
        cookie.setMaxAge(tokenMaxAge);
        if (isProd) {
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
        } else {
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
        }
        response.addCookie(cookie);
    }

    // 쿠키 삭제
    public void deleteQueueTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenKeyPrefix, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        if (isProd) {
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
        } else {
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
        }
        response.addCookie(cookie);
    }
}
