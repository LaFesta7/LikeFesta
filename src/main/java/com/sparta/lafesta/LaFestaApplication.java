package com.sparta.lafesta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class LaFestaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaFestaApplication.class, args);
    }

}
