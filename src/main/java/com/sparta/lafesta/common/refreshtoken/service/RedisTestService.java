package com.sparta.lafesta.common.refreshtoken.service;

import com.sparta.lafesta.common.refreshtoken.dto.RefreshTokenResponseDto;
import com.sparta.lafesta.common.refreshtoken.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTestService {

    // 의존성 주입
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    // 상수 선언
    private static final int LIMIT_TIME = 7 * 24 * 60 * 60; //일 * 시 * 분 * 초



    public RefreshTokenResponseDto createByRepository() {


        var responseDto =RefreshTokenResponseDto.builder()
                                .username("username")
                                .refreshToken("testToken")
                                .build();

        return refreshTokenRedisRepository.save(responseDto);

    }

    public void createByTemplate() {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
//        stringValueOperations.set("keyUsername", "valueRefreshToken");
        stringValueOperations.set("keyUsername", "valueRefreshToken", LIMIT_TIME, TimeUnit.SECONDS);

    }

    public String getRedis() {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        return stringValueOperations.get("keyUsername");
    }


    public void modify() {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.getAndSet("keyUsername", "modified");

    }


    public void delete() {
        redisTemplate.delete("keyUsername");
    }
}
