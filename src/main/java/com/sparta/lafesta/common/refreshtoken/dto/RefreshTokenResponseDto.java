package com.sparta.lafesta.common.refreshtoken.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("refreshTokenFolder")
public class RefreshTokenResponseDto {
    @Id
    private String username;

    private String refreshToken;
}
