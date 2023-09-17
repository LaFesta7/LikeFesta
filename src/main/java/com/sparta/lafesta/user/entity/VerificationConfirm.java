package com.sparta.lafesta.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "SignUp-Confirm", timeToLive = 30 * 60) //30분
public class VerificationConfirm {
    @Id
    private String email;
    private boolean confirm;
}
