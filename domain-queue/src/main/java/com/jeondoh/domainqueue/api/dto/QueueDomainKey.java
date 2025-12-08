package com.jeondoh.domainqueue.api.dto;

import com.jeondoh.queuecore.domain.DomainType;

public record QueueDomainKey(
        DomainType domainType,
        String resourceId,
        String memberId,
        String domainKey,
        String tokenKey,
        int tokenTTL
) {
    public static QueueDomainKey of(QueueEnterRequest request, String memberId, String tokenPrefix, int tokenTTL) {
        DomainType domainType = DomainType.valueOf(request.domain());
        String resourceId = request.resourceId();
        String domainKey = domainType.getDomainKey(resourceId, memberId);
        String tokenKey = tokenPrefix + ":" + domainKey;

        return new QueueDomainKey(
                domainType,
                resourceId,
                memberId,
                domainKey,
                tokenKey,
                tokenTTL
        );
    }

}
