package com.jeondoh.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(basePackages = {
        "com.jeondoh.core.servlet.model",
        "com.jeondoh.apiserver"
})
@SpringBootApplication(scanBasePackages = {
        "com.jeondoh.apiserver",
        "com.jeondoh.core.common",
        "com.jeondoh.core.servlet"
})
public class ApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
