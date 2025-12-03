package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterRequest;
import reactor.core.publisher.Mono;

public interface RouterService {

    // 요청 검증 → 대기열 대상 판단 → 라우팅 결정
    // - 대기열 대상이 아닌 경우: API 서버로 forwarding
    // - 대기열 대상인 경우: 대기열 필요 여부에 따라 redirect or forwarding
    Mono<ResponseApi<Object>> route(RouterRequest request);
}
