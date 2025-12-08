package com.jeondoh.core.reactive.component;

import com.jeondoh.core.common.exception.BaseException;
import com.jeondoh.core.common.exception.ReactiveException;
import com.jeondoh.core.reactive.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class ReactiveBaseExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // BaseException 처리
        if (ex instanceof BaseException baseEx) {
            return handleBaseException(exchange, baseEx);
        }

        log.error(ex.getMessage(), ex);
        return Mono.error(ReactiveException.reactiveException(exchange.getResponse().getStatusCode()));
    }

    private Mono<Void> handleBaseException(ServerWebExchange exchange, BaseException ex) {
        // 리다이렉트 처리
        if (ex.getStatusCode().is3xxRedirection()) {
            exchange.getResponse().setStatusCode(ex.getStatusCode());
            if (ex.getRedirectUrl() != null) {
                exchange.getResponse().getHeaders().setLocation(URI.create(ex.getRedirectUrl()));
            }
            return exchange.getResponse().setComplete();
        }

        // 응답 생성
        String finalMessage = ex.getDetailMessage() != null
                ? ex.getMessage() + " " + ex.getDetailMessage()
                : ex.getMessage();
        ResponseApi<Object> responseApi = ResponseApi.nok(ex.getStatusCode(), ex.getCode(), finalMessage);

        log.error("{}: {}", ex.getStatusCode(), finalMessage);

        // 응답 설정
        exchange.getResponse().setStatusCode(ex.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // ResponseApi 직렬화
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(responseApi);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            log.error("handleBaseException ResponseApi 직렬화 실패", e);
            return Mono.error(e);
        }
    }
}
