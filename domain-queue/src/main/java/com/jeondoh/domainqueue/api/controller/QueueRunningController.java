package com.jeondoh.domainqueue.api.controller;

import com.jeondoh.core.common.component.JwtDecoder;
import com.jeondoh.core.common.dto.JwtToken;
import com.jeondoh.core.reactive.ResponseApi;
import com.jeondoh.domainqueue.api.dto.QueueHeartbeat;
import com.jeondoh.domainqueue.api.dto.QueueHeartbeatRequest;
import com.jeondoh.domainqueue.domain.service.QueueRunningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jeondoh.core.common.util.StaticVariables.AUTH_HEADER_PREFIX_KEY;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueRunningController {

    private final JwtDecoder jwtDecoder;
    private final QueueRunningService queueRunningService;

    @PatchMapping("/heartbeat")
    public ResponseApi<Void> heartbeat(
            HttpServletRequest request,
            @Valid @RequestBody QueueHeartbeatRequest queueHeartbeatRequest
    ) {
        JwtToken decode = jwtDecoder.decode(request.getHeader(AUTH_HEADER_PREFIX_KEY));
        QueueHeartbeat queueHeartbeat = QueueHeartbeat.of(queueHeartbeatRequest, decode.memberId());
        queueRunningService.heartbeat(queueHeartbeat);
        return ResponseApi.ok();
    }
}
