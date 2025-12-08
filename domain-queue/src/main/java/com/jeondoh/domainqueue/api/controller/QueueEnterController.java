package com.jeondoh.domainqueue.api.controller;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.servlet.ResponseApi;
import com.jeondoh.domainqueue.api.dto.QueueEnterRequest;
import com.jeondoh.domainqueue.api.dto.QueueEnterResponse;
import com.jeondoh.domainqueue.core.util.CookieUtil;
import com.jeondoh.domainqueue.domain.model.QueueEntry;
import com.jeondoh.domainqueue.domain.service.QueueEnterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueEnterController {

    private final QueueEnterService queueEnterService;
    private final JwtDecoder jwtDecoder;
    private final CookieUtil cookieUtil;

    // 대기열 진입
    @PostMapping("/enter")
    public ResponseApi<QueueEnterResponse> enterQueue(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody QueueEnterRequest enterRequest
    ) {
        JwtToken decode = jwtDecoder.decode(request.getHeader(AUTH_HEADER_PREFIX_KEY));
        QueueEntry queueEntry = queueEnterService.enterQueue(enterRequest, decode.memberId());
        QueueEnterResponse queueEnterResponse = QueueEnterResponse.from(queueEntry.getQueueWaitOrder());

        cookieUtil.saveQueueTokenToCookie(response, queueEntry.getQueueDomainKey().tokenKey());

        return ResponseApi.ok(queueEnterResponse);
    }
}
