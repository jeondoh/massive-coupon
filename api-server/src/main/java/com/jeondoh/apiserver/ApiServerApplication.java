package com.jeondoh.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.apiserver",
        "com.jeondoh.core.common.component",
        "com.jeondoh.core.servlet.component",
        "com.jeondoh.core.common.infrastructure.rabbitmq"
})
public class ApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
