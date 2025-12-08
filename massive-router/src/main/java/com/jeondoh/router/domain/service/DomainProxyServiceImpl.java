package com.jeondoh.router.domain.service;

import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.RouterServerHttp;
import com.jeondoh.router.domain.exception.ForwardingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DomainProxyServiceImpl implements DomainProxyService {

    public DomainProxyServiceImpl(
            WebClient webClient,
            @Value("${domain.server.api-url}") String apiServerUrl,
            @Value("${domain.server.queue-url}") String queueServerUrl
    ) {
        this.webClient = webClient;
        this.apiServerUrl = apiServerUrl;
        this.queueServerUrl = queueServerUrl;
    }

    private final WebClient webClient;
    private final String apiServerUrl;
    private final String queueServerUrl;


    // API 서버로 포워딩
    @Override
    public Mono<ResponseApi<Object>> forwardToApiServer(RouterServerHttp serverHttp) {
        return forwardRequest(serverHttp, apiServerUrl);
    }

    // Queue 서버로 포워딩
    @Override
    public Mono<ResponseApi<Object>> forwardToQueue(RouterServerHttp serverHttp) {
        return forwardRequest(serverHttp, queueServerUrl);
    }

    // request 요청을 서버로 포워딩
    // - 원본 요청 그대로 전달
    // - 모든 예외는 BaseException을 던져 ReactiveBaseExceptionHandler에서 처리
    private Mono<ResponseApi<Object>> forwardRequest(RouterServerHttp serverHttp, String baseUrl) {
        final String targetUrl = createTargetUrl(serverHttp.request(), baseUrl);

        log.trace("포워딩 URL: {}", targetUrl);

        return webClient
                .method(serverHttp.request().getMethod())
                .uri(targetUrl)
                .headers(headers -> headers.addAll(serverHttp.request().getHeaders()))
                .body(serverHttp.request().getBody(), DataBuffer.class)
                .exchangeToMono(clientResponse -> {
                    serverHttp.response().setStatusCode(clientResponse.statusCode());
                    clientResponse.headers().asHttpHeaders().forEach((key, values) -> {
                        if (!key.equalsIgnoreCase("content-length")) {
                            values.forEach(value -> serverHttp.response().getHeaders().add(key, value));
                        }
                    });
                    // 서버의 상태 코드 확인
                    if (clientResponse.statusCode().isError()) {
                        // 에러 응답인 경우
                        return clientResponse.bodyToMono(new ParameterizedTypeReference<ResponseApi<Object>>() {
                                })
                                .flatMap(errorResponse ->
                                        Mono.error(ForwardingException.forwardApiServerException(
                                                errorResponse.getCode(),
                                                errorResponse.getMessage(),
                                                clientResponse.statusCode()
                                        ))
                                );
                    } else {
                        // 성공 응답인 경우
                        return clientResponse.bodyToMono(new ParameterizedTypeReference<>() {
                        });
                    }
                });
    }

    // API 서버 주소 생성
    private String createTargetUrl(ServerHttpRequest request, String baseUrl) {
        String url = baseUrl + request.getURI().getPath();
        if (request.getURI().getQuery() != null) {
            url += "?" + request.getURI().getQuery();
        }
        return url;
    }
}
