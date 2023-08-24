package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class SelectUserResponseDto {
    Long id;
    String username;
    String nickname;
    UserRoleEnum role;

    public SelectUserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
