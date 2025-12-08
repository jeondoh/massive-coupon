package com.jeondoh.domainqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.domainqueue",
        "com.jeondoh.core.common.component",
        "com.jeondoh.core.servlet.component",
        "com.jeondoh.queuecore.component"
})
public class DomainQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomainQueueApplication.class, args);
    }

}
