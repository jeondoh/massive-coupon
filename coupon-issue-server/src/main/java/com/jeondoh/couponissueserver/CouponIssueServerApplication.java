package com.jeondoh.couponissueserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.couponissueserver",
        "com.jeondoh.core.common.component",
        "com.jeondoh.core.common.infrastructure"
})
public class CouponIssueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponIssueServerApplication.class, args);
    }

}
