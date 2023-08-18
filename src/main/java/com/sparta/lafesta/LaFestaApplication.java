package com.sparta.lafesta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LaFestaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaFestaApplication.class, args);
    }

}
