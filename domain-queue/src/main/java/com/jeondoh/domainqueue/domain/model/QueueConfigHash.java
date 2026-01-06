package com.jeondoh.domainqueue.domain.model;

import com.jeondoh.core.common.dto.DomainType;
import com.jeondoh.domainqueue.api.dto.QueueConfigRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import static com.jeondoh.core.common.util.StaticVariables.CONFIG_KEY;

@Getter
@Builder
@RedisHash(value = CONFIG_KEY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueueConfigHash {

    @Id
    private String id;

    private DomainType domainType;
    private String resourceId;

    private Integer threshold;
    private Integer transferSize;
    private Integer trafficRpm;

    public static QueueConfigHash from(QueueConfigRequest request) {
        String id = createId(request.domain(), request.resourceId());

        return QueueConfigHash.builder()
                .id(id)
                .domainType(DomainType.valueOf(request.domain()))
                .resourceId(request.resourceId())
                .threshold(request.threshold())
                .transferSize(request.transferSize())
                .trafficRpm(request.trafficRpm())
                .build();
    }

    public void updateFieldValue(QueueConfigRequest request) {
        this.threshold = request.threshold();
        this.transferSize = request.transferSize();
        this.trafficRpm = request.trafficRpm();
    }

    public static String createId(String domain, String resourceId) {
        DomainType domainType = DomainType.valueOf(domain);
        return String.format("%s:%s", domainType.name(), resourceId);
    }
}
