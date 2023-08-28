package com.sparta.lafesta.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPasswordRequestDto {
    private String password;
    private String modifyPassword;
}
