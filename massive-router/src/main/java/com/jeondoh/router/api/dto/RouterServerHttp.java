package com.jeondoh.router.api.dto;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public record RouterServerHttp(
        ServerHttpRequest request,
        ServerHttpResponse response
) {
    public static RouterServerHttp from(RouterRequest request) {
        return new RouterServerHttp(request.request(), request.response());
    }
}
