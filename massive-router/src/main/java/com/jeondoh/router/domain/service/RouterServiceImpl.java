package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.QueueConfigExists;
import com.jeondoh.router.api.dto.QueueTargetMatch;
import com.jeondoh.router.api.dto.RouterRequest;
import com.jeondoh.router.api.dto.RouterServerHttp;
import com.jeondoh.router.domain.exception.RouterException;
import com.jeondoh.router.domain.model.QueueTargetMatcher;
import com.jeondoh.router.domain.model.RoutingDecision;
import com.jeondoh.router.domain.model.RoutingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RouterServiceImpl implements RouterService {

    private final String redirectUrl;
    private final QueueTargetMatcher queueTargetMatcher;
    private final RoutingDecision routingDecision;
    private final DomainProxyService domainProxyService;

    public RouterServiceImpl(
            @Value("${domain.client.redirect-url}") String redirectUrl,
            QueueTargetMatcher queueTargetMatcher,
            RoutingDecision routingDecision,
            DomainProxyService domainProxyService
    ) {
        this.redirectUrl = redirectUrl;
        this.queueTargetMatcher = queueTargetMatcher;
        this.routingDecision = routingDecision;
        this.domainProxyService = domainProxyService;
    }

    // 요청 검증 → 대기열 대상 판단 → 라우팅 결정
    // - 대기열 대상이 아닌 경우: API 서버로 forwarding
    // - 대기열 대상인 경우: 대기열 필요 여부에 따라 redirect or forwarding
    @Override
    public Mono<ResponseApi<Object>> route(RouterRequest request) {
        RouterServerHttp serverHttp = RouterServerHttp.from(request);
        return queueTargetMatcher.match(request.path())
                .flatMap(queueTarget -> routeWithQueueCheck(serverHttp, queueTarget));
    }

    // 대기열 필요 여부에 따라 라우팅
    private Mono<ResponseApi<Object>> routeWithQueueCheck(RouterServerHttp serverHttp, QueueTargetMatch queueTarget) {
        QueueConfigExists queueConfig = QueueConfigExists.of(queueTarget.domain(), queueTarget.resourceId());
        return routingDecision.decide(queueConfig)
                .flatMap(decision -> executeRoutingDecision(serverHttp, decision));
    }

    // 라우팅 결정
    private Mono<ResponseApi<Object>> executeRoutingDecision(RouterServerHttp serverHttp, RoutingType routingType) {
        return switch (routingType) {
            // 대기열이 필요한 경우: 클라이언트에 리다이렉트 예외 발생
            case REDIRECT_TO_QUEUE -> Mono.error(RouterException.QueueRequiredException(redirectUrl));
            // 대기열이 불필요한 경우: API 서버로 포워딩
            case FORWARD_TO_BACKEND -> domainProxyService.forwardRequest(serverHttp);
        };
    }

}
