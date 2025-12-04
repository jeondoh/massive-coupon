package com.jeondoh.domainqueue.api.controller;

import com.jeondoh.core.servlet.ResponseApi;
import com.jeondoh.domainqueue.api.dto.QueueConfigDeleteRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigSaveRequest;
import com.jeondoh.domainqueue.api.dto.QueueConfigUpdateRequest;
import com.jeondoh.domainqueue.domain.service.QueueConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/queue/config")
@RequiredArgsConstructor
public class QueueConfigController {

    private final QueueConfigService queueConfigService;

    // 대기열 설정 영구저장
    @PostMapping
    public ResponseApi<Void> saveConfig(@Valid @RequestBody QueueConfigSaveRequest request) {
        queueConfigService.saveConfig(request);
        return ResponseApi.ok();
    }

    // 대기열 설정 업데이트
    @PatchMapping
    public ResponseApi<Void> updateConfigField(@Valid @RequestBody QueueConfigUpdateRequest request) {
        queueConfigService.updateConfigField(request);
        return ResponseApi.ok();
    }

    // 대기열 설정 삭제
    @DeleteMapping
    public ResponseApi<Void> deleteConfig(@Valid @RequestBody QueueConfigDeleteRequest request) {
        queueConfigService.deleteConfig(request);
        return ResponseApi.ok();
    }
}
