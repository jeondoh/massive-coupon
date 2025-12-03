package com.jeondoh.router.api.dto;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public record RouterRequest(
        String path,
        ServerHttpRequest request,
        ServerHttpResponse response
) {
    public static RouterRequest of(String path, ServerHttpRequest request, ServerHttpResponse response) {
        return new RouterRequest(path, request, response);
    }
}
