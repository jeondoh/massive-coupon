package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterServerHttp;
import reactor.core.publisher.Mono;

public interface DomainProxyService {

    // request 요청을 API 서버로 포워딩
    // - 원본 요청 그대로 전달
    Mono<ResponseApi<Object>> forwardRequest(RouterServerHttp serverHttp);
}
