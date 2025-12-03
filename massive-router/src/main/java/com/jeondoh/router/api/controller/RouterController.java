package com.jeondoh.router.api.controller;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterRequest;
import com.jeondoh.router.domain.service.RouterService;
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

    private final RouterService routerService;

    @RequestMapping("/**")
    public Mono<ResponseApi<Object>> route(ServerHttpRequest request, ServerHttpResponse response) {
        String fullPath = request.getURI().getPath();
        RouterRequest routerRequest = RouterRequest.of(fullPath, request, response);
        return routerService.route(routerRequest);
    }

}
