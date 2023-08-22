package com.sparta.lafesta.social.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginDto { // 카카오 로그인 시 응답받는 DTO
    private Long id;
    private String nickname;
    private String email;

    public KakaoLoginDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
