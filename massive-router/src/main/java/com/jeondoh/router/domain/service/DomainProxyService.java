package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterServerHttp;
import reactor.core.publisher.Mono;

public interface DomainProxyService {

    // API 서버로 포워딩
    Mono<ResponseApi<Object>> forwardToApiServer(RouterServerHttp serverHttp);

    // Queue 서버로 포워딩
    Mono<ResponseApi<Object>> forwardToQueue(RouterServerHttp serverHttp);
}
