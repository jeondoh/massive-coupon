package com.jeondoh.router.api.dto;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public record RouterServerHttp(
        ServerHttpRequest request,
        ServerHttpResponse response
) {
    public static RouterServerHttp of(ServerHttpRequest request, ServerHttpResponse response) {
        return new RouterServerHttp(request, response);
    }

}
