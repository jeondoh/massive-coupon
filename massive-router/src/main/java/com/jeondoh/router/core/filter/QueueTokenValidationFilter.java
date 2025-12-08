package com.jeondoh.router.core.filter;

import com.jeondoh.router.core.util.QueueTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-50)
@Component
public class QueueTokenValidationFilter implements WebFilter {

    public QueueTokenValidationFilter(
            QueueTokenHelper queueTokenHelper,
            @Value("${queue.token.cookie-name}") String cookieName,
            @Value("${queue.token.attr-valid-key}") String attrValidKey,
            @Value("${queue.token.attr-value-key}") String attrValueKey
    ) {
        this.queueTokenHelper = queueTokenHelper;
        this.cookieName = cookieName;
        this.attrValidKey = attrValidKey;
        this.attrValueKey = attrValueKey;
    }

    private final QueueTokenHelper queueTokenHelper;
    private final String cookieName;
    private final String attrValidKey;
    private final String attrValueKey;

    // Token 유효성 검증 필터
    // - Cookie에서 Token 추출
    // - Redis에서 Token 유효성 검증
    // - 유효시 ServerWebExchange Attribute 설정
    // - TTL 갱신
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpCookie httpCookie = exchange.getRequest().getCookies().getFirst(cookieName);

        return Mono.justOrEmpty(httpCookie)
                .flatMap(queueTokenHelper::extractTokenFromCookie)
                .flatMap(token -> queueTokenHelper.validateTokenInRedis(token)
                        .filter(Boolean::booleanValue)
                        .doOnNext(valid -> {
                            // Token 유효
                            // - attribute 추가, 이후 QueueRequirementFilter에서 사용
                            exchange.getAttributes().put(attrValidKey, true);
                            exchange.getAttributes().put(attrValueKey, token);
                        })
                )
                .then(chain.filter(exchange));
    }

}
