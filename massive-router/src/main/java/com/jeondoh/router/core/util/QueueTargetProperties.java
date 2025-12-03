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
    private List<QueueTarget> targets = new ArrayList<>();

    @Getter
    @Setter
    public static class QueueTarget {
        private String pattern;
        private String domain;
    }
}
