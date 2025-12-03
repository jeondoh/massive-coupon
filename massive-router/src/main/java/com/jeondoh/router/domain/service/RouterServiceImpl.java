package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterRequest;
import com.jeondoh.router.api.dto.RouterServerHttp;
import com.jeondoh.router.core.config.QueueTargetProperties;
import com.jeondoh.router.domain.exception.RouterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class RouterServiceImpl implements RouterService {

    public RouterServiceImpl(
            QueueTargetProperties queueTargetProperties,
            DomainProxyService domainProxyService,
            @Value("${domain.client.redirect-url}") String redirectUrl
    ) {
        this.queueTargetProperties = queueTargetProperties;
        this.domainProxyService = domainProxyService;
        this.pathMatcher = new AntPathMatcher();
        this.redirectUrl = redirectUrl;
    }

    private final QueueTargetProperties queueTargetProperties;
    private final DomainProxyService domainProxyService;
    private final AntPathMatcher pathMatcher;
    private final String redirectUrl;

    // 요청을 처리하여 응답을 반환
    @Override
    public Mono<ResponseApi<Object>> route(RouterRequest request) {
        // 요청 검증
        String requestPath = request.path();
        if (requestPath == null || requestPath.isEmpty()) {
            return Mono.error(RouterException.InvalidPathException(request.path()));
        }

        RouterServerHttp serverHttp = RouterServerHttp.from(request);

        // 대기열 대상이 아닌 경우
        // - API 서버로 포워딩
        if (!isQueueTarget(requestPath)) {
            return domainProxyService.forwardRequest(serverHttp);
        }

        // 대기열 대상인 경우
        // - 대기열 필요 여부 판단
        return isQueueRequired(requestPath)
                .flatMap(needsQueue -> {
                    if (needsQueue) {
                        // 대기열이 필요한 경우
                        // - 클라이언트 리다이렉트
                        return Mono.error(RouterException.QueueRequiredException(redirectUrl));
                    }
                    // 대기열이 불필요한 경우
                    // - API 서버로 포워딩
                    return domainProxyService.forwardRequest(serverHttp);
                });
    }

    // todo: 대기열 필요 여부 판단
    @Override
    public Mono<Boolean> isQueueRequired(String path) {
        return Mono.just(false);
    }

    // URL이 대기열 대상인지 판단
    @Override
    public boolean isQueueTarget(String path) {
        List<String> targets = queueTargetProperties.getTargets();
        return targets.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

}

