package com.sparta.lafesta.user.entity;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "Verification-Code", timeToLive = 3 * 60) //3분
public class VerificationCode {
    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String code;

    public VerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}