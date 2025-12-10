package com.jeondoh.domainqueue.api.controller;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.domainqueue.api.dto.QueueDomainMember;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrderRequest;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrderResponse;
import com.jeondoh.domainqueue.domain.service.QueueWaitOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueWaitOrderController {

    private final JwtDecoder jwtDecoder;
    private final QueueWaitOrderService queueWaitOrderService;

    // 대기순번 가져오기
    @GetMapping("/order")
    public ResponseApi<QueueWaitOrderResponse> memberWaitOrder(
            HttpServletRequest request,
            @Valid QueueWaitOrderRequest queueWaitOrderRequest
    ) {
        JwtToken jwtToken = jwtDecoder.decode(request.getHeader(AUTH_HEADER_PREFIX_KEY));
        QueueDomainMember queueWaitOrder = QueueDomainMember.of(
                jwtToken.memberId(),
                queueWaitOrderRequest.domain(),
                queueWaitOrderRequest.resourceId()
        );

        QueueWaitOrderResponse response = queueWaitOrderService.waitOrder(queueWaitOrder);
        return ResponseApi.ok(response);
    }
}
