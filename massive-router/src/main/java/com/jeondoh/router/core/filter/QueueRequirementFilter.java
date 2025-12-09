package com.jeondoh.router.core.filter;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
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

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@Slf4j
@Order(-10)
@Component
public class QueueRequirementFilter implements WebFilter {

    public QueueRequirementFilter(
            QueueTargetMatcher queueTargetMatcher,
            RouterService routerService,
            JwtDecoder jwtDecoder,
            @Value("${domain.client.redirect-url}") String redirectUrl
    ) {
        this.queueTargetMatcher = queueTargetMatcher;
        this.routerService = routerService;
        this.jwtDecoder = jwtDecoder;
        this.redirectUrl = redirectUrl;
    }

    private final QueueTargetMatcher queueTargetMatcher;
    private final RouterService routerService;
    private final JwtDecoder jwtDecoder;
    private final String redirectUrl;

    // 대기열 검증 필터
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER_PREFIX_KEY);

        Mono<Boolean> needsQueueMono = queueTargetMatcher.matchTarget(path)
                .flatMap(target -> {
                    JwtToken jwtToken = jwtDecoder.decode(authHeader);
                    // Running Queue 확인
                    return routerService.checkMemberInRunningQueue(target, jwtToken.memberId())
                            .flatMap(isInRunning -> {
                                if (isInRunning) {
                                    return Mono.just(false);
                                }
                                // Running Queue 에 없으면 대기열 필요 여부 체크
                                return routerService.checkQueueRequired(target);
                            });
                })
                .defaultIfEmpty(false);

        return needsQueueMono.flatMap(needsQueue -> {
            if (needsQueue) {
                return Mono.error(RouterException.QueueRequiredException(redirectUrl));
            }
            return chain.filter(exchange);
        });
    }
}
