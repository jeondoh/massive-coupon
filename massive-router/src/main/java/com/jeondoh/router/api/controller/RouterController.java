package com.jeondoh.router.api.controller;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterServerHttp;
import com.jeondoh.router.domain.service.DomainProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RouterController {

    private final DomainProxyService domainProxyService;

    // 필터에서 검증된 요청 domain-queue 서버로 포워딩
    @RequestMapping("/queue/**")
    public Mono<ResponseApi<Object>> routeQueue(ServerHttpRequest request, ServerHttpResponse response) {
        RouterServerHttp routerServerHttp = RouterServerHttp.of(request, response);
        return domainProxyService.forwardToQueue(routerServerHttp);
    }

    // 필터에서 검증된 요청 API 서버로 포워딩
    @RequestMapping("/**")
    public Mono<ResponseApi<Object>> routeApi(ServerHttpRequest request, ServerHttpResponse response) {
        RouterServerHttp routerServerHttp = RouterServerHttp.of(request, response);
        return domainProxyService.forwardToApiServer(routerServerHttp);
    }

}
