package com.jeondoh.router.core.filter;

import com.jeondoh.router.domain.exception.RouterException;
import com.jeondoh.router.domain.model.QueueTargetMatcher;
import com.jeondoh.router.domain.service.RouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-10)
@Component
public class QueueRequirementFilter implements WebFilter {

    public QueueRequirementFilter(
            QueueTargetMatcher queueTargetMatcher,
            RouterService routerService,
            @Value("${queue.token.attr-valid-key}") String attrValidKey,
            @Value("${domain.client.redirect-url}") String redirectUrl
    ) {
        this.queueTargetMatcher = queueTargetMatcher;
        this.routerService = routerService;
        this.attrValidKey = attrValidKey;
        this.redirectUrl = redirectUrl;
    }

    private final QueueTargetMatcher queueTargetMatcher;
    private final RouterService routerService;
    private final String attrValidKey;
    private final String redirectUrl;

    // 대기열 검증 필터
    // - Token 유효시 대기열 체크 스킵
    // - 대기열 진입 대상 URL(queue.targets) -> 부하 체크
    // - 대기열 토큰 검증 대상 URL(queue.authorization) -> Token 필수
    // - 그 외 URL -> 그대로 요청 포워딩
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 토큰 유효시 스킵
        Boolean queueValid = exchange.getAttribute(attrValidKey);
        if (Boolean.TRUE.equals(queueValid)) {
            return chain.filter(exchange);
        }
        // 토큰 유효하지 않음 & 대기열 진입 대상인지 확인
        String path = exchange.getRequest().getURI().getPath();
        return queueTargetMatcher.matchTarget(path)
                .flatMap(target ->
                        routerService.checkQueueRequired(target)
                                .flatMap(needsQueue -> {
                                    if (needsQueue) {
                                        // 대기열 필요
                                        return Mono.error(RouterException.QueueRequiredException(redirectUrl));
                                    }
                                    // 대기열 불필요
                                    return Mono.just(true);
                                })
                )
                .defaultIfEmpty(false)
                .flatMap(isTarget -> {
                    if (isTarget) {
                        return chain.filter(exchange);
                    }
                    // 토큰 검증 대상인지 확인
                    return queueTargetMatcher.matchQueueToken(path)
                            .flatMap(tokenRequired -> {
                                if (tokenRequired) {
                                    // 토큰이 필요하지만 존재하지 않음
                                    return Mono.error(RouterException.QueueTokenRequiredException());
                                }
                                return chain.filter(exchange);
                            });
                });

    }
}
