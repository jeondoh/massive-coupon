package com.jeondoh.router.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class QueueTokenHelper {

    public QueueTokenHelper(
            ReactiveRedisTemplate<String, String> reactiveRedisTemplate,
            @Value("${queue.token.key-prefix}") String keyPrefix,
            @Value("${queue.token.ttl-seconds}") int ttlSeconds
    ) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.keyPrefix = keyPrefix;
        this.ttlSeconds = ttlSeconds;
    }

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final String keyPrefix;
    private final int ttlSeconds;

    // cookie에서 token 추출
    public Mono<String> extractTokenFromCookie(HttpCookie httpCookie) {
        return Mono.justOrEmpty(httpCookie)
                .map(HttpCookie::getValue);
    }

    // redis에서 token 유효성 확인
    public Mono<Boolean> validateTokenInRedis(String key) {
        return reactiveRedisTemplate.hasKey(key)
                .flatMap(exists -> {
                    if (exists) {
                        // TTL 갱신
                        return reactiveRedisTemplate.expire(key, Duration.ofSeconds(ttlSeconds))
                                .thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }
}
