package com.jeondoh.queuecore.utils;

public class StaticVariables {
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
    // 이동 주기 ex) waiting queue -> running queue
    // - 스케줄러 실행 주기
    public static final String CONFIG_TRANSFER_INTERVAL = "transferInterval";

    // ========================================================================
    // KEY값
    public static final String CONFIG_KEY = "config";
    public static final String TRAFFIC_KEY = "traffic";
}
