package com.jeondoh.router.core.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "queue")
public class QueueTargetProperties {
    // 대기열 진입 체크 대상 URL
    private List<QueueTarget> targets = new ArrayList<>();
    // 대기열 진입 후 토큰 체크 대상 URL
    private List<QueueTarget> authorization = new ArrayList<>();

    @Getter
    @Setter
    public static class QueueTarget {
        private String pattern;
        private String domain;
    }
}
