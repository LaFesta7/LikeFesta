package com.sparta.lafesta.common.refreshtoken.service;

import com.sparta.lafesta.common.refreshtoken.dto.RefreshTokenResponseDto;
import com.sparta.lafesta.common.refreshtoken.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTestService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public RefreshTokenResponseDto createByRepository() {


        var responseDto =RefreshTokenResponseDto.builder()
                                .username("username")
                                .refreshToken("testToken")
                                .build();

        return refreshTokenRedisRepository.save(responseDto);

    }

    public void createByTemplate() {

        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();

        stringValueOperations.set("keyUsername", "valueRefreshToken");

    }




}
