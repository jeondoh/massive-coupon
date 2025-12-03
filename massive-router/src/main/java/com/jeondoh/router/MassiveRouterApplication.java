package com.jeondoh.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.router",
        "com.jeondoh.core.common.component",
        "com.jeondoh.core.reactive.component",
        "com.jeondoh.queuecore.component"
})
public class MassiveRouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MassiveRouterApplication.class, args);
    }

}
