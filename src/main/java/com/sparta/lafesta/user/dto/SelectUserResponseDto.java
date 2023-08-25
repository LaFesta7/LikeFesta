package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class SelectUserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private UserRoleEnum role;
    private List<FileOnS3Dto> files;

    public SelectUserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.files = user.getUserFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
    }
}
