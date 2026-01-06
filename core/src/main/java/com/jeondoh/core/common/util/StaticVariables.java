package com.jeondoh.core.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticVariables {

    // ========================================================================
    // JWT
    public static final String ROLE_CLAIM_KEY = "authorities";
    public static final String NAME_CLAIM_KEY = "username";
    public static final String AUTH_HEADER_PREFIX_KEY = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    // ========================================================================
    // KEY값
    public static final String CONFIG_KEY = "config";
    public static final String TRAFFIC_KEY = "traffic";

    // ========================================================================
    // MQ Argument
    public static final String DLQ_EXCHANGE_KEY = "x-dead-letter-exchange";
    public static final String DLQ_ROUTING_KEY = "x-dead-letter-routing-key";

    // ========================================================================
    // CONFIG VALUE

    // 임계값
    // - 동시에 처리할 수 있는 최대 사용자 수
    // - 큐 최대 크기
    public static final String CONFIG_THRESHOLD = "threshold";
    // 트래픽 임계값
    // - 분당 요청 수
    public static final String CONFIG_TRAFFIC_RPM = "trafficRpm";
    // 이동 크기
    // - 이동시킬 사용자 수
    public static final String CONFIG_TRANSFER_SIZE = "transferSize";

}
