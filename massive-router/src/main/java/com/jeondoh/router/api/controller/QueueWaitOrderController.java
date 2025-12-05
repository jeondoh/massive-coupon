package com.jeondoh.router.api.controller;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.router.api.dto.QueueWaitOrder;
import com.jeondoh.router.api.dto.QueueWaitOrderRequest;
import com.jeondoh.router.api.dto.QueueWaitOrderResponse;
import com.jeondoh.router.domain.service.QueueWaitOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueWaitOrderController {

    private final JwtDecoder jwtDecoder;
    private final QueueWaitOrderService queueWaitOrderService;

    // 대기순번 가져오기
    @GetMapping("/order")
    public Mono<ResponseApi<QueueWaitOrderResponse>> memberWaitOrder(
            ServerHttpRequest request,
            @Valid QueueWaitOrderRequest queueWaitOrderRequest
    ) {
        JwtToken jwtToken = jwtDecoder.decode(request.getHeaders().getFirst(AUTH_HEADER_PREFIX_KEY));
        QueueWaitOrder queueWaitOrder = QueueWaitOrder.of(
                jwtToken.memberId(),
                queueWaitOrderRequest.domain(),
                queueWaitOrderRequest.resourceId()
        );
        return queueWaitOrderService.waitOrder(queueWaitOrder)
                .map(ResponseApi::ok);
    }
}
