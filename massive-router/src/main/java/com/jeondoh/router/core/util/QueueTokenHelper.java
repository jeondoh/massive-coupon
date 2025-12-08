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
            @Value("${queue.token.key-name}") String keyName,
            @Value("${queue.token.ttl-seconds}") int ttlSeconds
    ) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.keyName = keyName;
        this.ttlSeconds = ttlSeconds;
    }

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final String keyName;
    private final int ttlSeconds;

    // cookie에서 token 추출
    public Mono<String> extractTokenFromCookie(HttpCookie httpCookie) {
        return Mono.justOrEmpty(httpCookie)
                .map(HttpCookie::getValue);
    }

    // redis에서 token 유효성 확인
    public Mono<Boolean> validateTokenInRedis(String token) {
        String key = keyName + ":" + token;
        return reactiveRedisTemplate.hasKey(key)
                .flatMap(exists -> {
                    if (exists) {
                        // TTL 갱신
                        return reactiveRedisTemplate.expire(key, Duration.ofSeconds(ttlSeconds))
                                .thenReturn(true);
                    } else {
                        log.warn("Queue Token이 없거나 만료되었습니다. {}", key);
                        return Mono.just(false);
                    }
                });
    }
}
