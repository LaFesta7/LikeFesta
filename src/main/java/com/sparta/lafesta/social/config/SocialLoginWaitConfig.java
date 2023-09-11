package com.sparta.lafesta.social.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class SocialLoginWaitConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder // login wait time 의 timeout 설정
                .setConnectTimeout(Duration.ofSeconds(7))
                .setReadTimeout(Duration.ofSeconds(7))
                .build();
    }
}