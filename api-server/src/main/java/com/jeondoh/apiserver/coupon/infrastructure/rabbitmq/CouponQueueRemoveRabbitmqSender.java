package com.jeondoh.apiserver.coupon.infrastructure.rabbitmq;

public interface CouponQueueRemoveRabbitmqSender {

    // running queue 멤버 제거 요청
    void sendRemoveRunningQueue(String resourceId, String memberId);
}
