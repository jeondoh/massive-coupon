package com.jeondoh.couponissueserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(basePackages = {
        "com.jeondoh.core.servlet.model",
        "com.jeondoh.couponissueserver"
})
@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.couponissueserver",
        "com.jeondoh.core.common",
        "com.jeondoh.core.servlet"
})
public class CouponIssueServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponIssueServerApplication.class, args);
    }

}
