package com.jeondoh.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.router",
        "com.jeondoh.core.common",
        "com.jeondoh.core.reactive"
})
public class MassiveRouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MassiveRouterApplication.class, args);
    }

}
