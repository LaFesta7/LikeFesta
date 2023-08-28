package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.User;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto extends ApiResponseDto {
    String username;
    String password;
    String email;

    public UserProfileResponseDto(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();

    }
}
