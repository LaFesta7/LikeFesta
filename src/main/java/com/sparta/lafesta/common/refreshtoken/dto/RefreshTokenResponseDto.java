package com.sparta.lafesta.common.refreshtoken.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 7*24*60*60) //일.시.분.초 // 7일로 설정
public class RefreshTokenResponseDto {
    @Id
    private String username;

    private String refreshToken;
}
