package com.sparta.lafesta.common.refreshtoken.controller;

import com.sparta.lafesta.common.refreshtoken.dto.RefreshTokenResponseDto;
import com.sparta.lafesta.common.refreshtoken.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisTestService redisTestService;

    @GetMapping("/redis/test-create-repo")
    public RefreshTokenResponseDto testCreateByRepo() {
        return redisTestService.createByRepository();
    }

    @GetMapping("/redis/test-create-template")
    public void testCreateByTemp() {
        redisTestService.createByTemplate();
    }

    @GetMapping("/redis/test-get-template")
    public String testGetByTemp() {
        return redisTestService.getRedis();
    }

    @GetMapping("/redis/test-modify-template")
    public void testModifyByTemp() {
        redisTestService.modify();
    }

    @GetMapping("/redis/test-delete-template")
    public void testDeleteByTemp() {
        redisTestService.delete();
    }
}
