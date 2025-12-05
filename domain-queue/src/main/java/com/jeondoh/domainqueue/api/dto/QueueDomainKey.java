package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueDomainKey(
        DomainType domainType,
        String resourceId,
        String memberId,
        String domainKey
) {
    public static QueueDomainKey of(QueueEnterRequest request, String memberId) {
        DomainType domainType = DomainType.valueOf(request.domain());
        String resourceId = request.resourceId();

        return new QueueDomainKey(
                domainType,
                resourceId,
                memberId,
                domainType.getDomainKey(resourceId, memberId)
        );
    }

}
