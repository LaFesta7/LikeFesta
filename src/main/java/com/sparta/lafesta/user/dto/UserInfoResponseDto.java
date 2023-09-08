package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {
    private Long id;
    private String role;
    private String username;
    private String email;
    private String nickname;
    private List<FileOnS3Dto> files;

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.role = user.getRole().getAuthority();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.files = user.getUserFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
    }
}
