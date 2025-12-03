package com.jeondoh.router.api.dto;

import com.jeondoh.router.domain.exception.RouterException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public record RouterRequest(
        String path,
        ServerHttpRequest request,
        ServerHttpResponse response
) {
    public static RouterRequest validateOf(String path, ServerHttpRequest request, ServerHttpResponse response) {
        if (path == null || path.isEmpty()) {
            throw RouterException.InvalidPathException(path);
        }
        return new RouterRequest(path, request, response);
    }
}
