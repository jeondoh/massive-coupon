package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterRequest;
import reactor.core.publisher.Mono;

public interface RouterService {

    // 요청을 처리하여 응답을 반환
    // - 대기열 필요: 대기열 서버로 요청
    // - 대기열 불필요: API 서버로 포워딩
    Mono<ResponseApi<Object>> route(RouterRequest request);

    // 대기열 필요 여부 판단
    Mono<Boolean> isQueueRequired(String path);

    // URL이 대기열 대상인지 판단
    boolean isQueueTarget(String path);
}
