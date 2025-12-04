package com.jeondoh.domainqueue.api.controller;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.servlet.ResponseApi;
import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.api.dto.QueueEnterResponse;
import com.jeondoh.domainqueue.api.dto.QueueWaitOrder;
import com.jeondoh.domainqueue.domain.service.QueueEnterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueEnterController {

    private final QueueEnterService queueEnterService;
    private final JwtDecoder jwtDecoder;

    // 대기열 진입
    @PostMapping("/enter")
    public ResponseApi<QueueEnterResponse> enterQueue(
            HttpServletRequest request,
            @Valid @RequestBody QueueEnterRequest enterRequest
    ) {
        JwtToken decode = jwtDecoder.decode(request.getHeader(AUTH_HEADER_PREFIX_KEY));
        QueueWaitOrder queueWaitOrder = queueEnterService.enterQueue(enterRequest, decode.memberId());
        QueueEnterResponse queueEnterResponse = QueueEnterResponse.from(queueWaitOrder);
        return ResponseApi.ok(queueEnterResponse);
    }
}
