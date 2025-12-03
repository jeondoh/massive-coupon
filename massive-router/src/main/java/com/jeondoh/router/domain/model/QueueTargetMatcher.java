package com.jeondoh.router.domain.model;

import com.jeondoh.router.api.dto.QueueTargetMatch;
import com.jeondoh.router.core.util.QueueTargetProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class QueueTargetMatcher {

    private final AntPathMatcher pathMatcher;
    private final QueueTargetProperties queueTargetProperties;

    public QueueTargetMatcher(QueueTargetProperties queueTargetProperties) {
        this.pathMatcher = new AntPathMatcher();
        this.queueTargetProperties = queueTargetProperties;
    }

    // 요청 경로가 대기열 대상인지 확인하고 매칭 정보 반환
    // - 설정된 대기열 대상 패턴과 비교
    public Mono<QueueTargetMatch> match(String path) {
        List<QueueTargetProperties.QueueTarget> targets = queueTargetProperties.getTargets();
        for (QueueTargetProperties.QueueTarget target : targets) {
            if (pathMatcher.match(target.getPattern(), path)) {
                // 경로에서 resourceId 추출
                Map<String, String> resourceMap = pathMatcher.extractUriTemplateVariables(target.getPattern(), path);
                QueueTargetMatch match = QueueTargetMatch.of(
                        target.getDomain(),
                        resourceMap.get("id")
                );
                return Mono.just(match);
            }
        }
        return Mono.empty();
    }

}
