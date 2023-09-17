package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.common.entity.StringFormatter;
import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {
    private Long id;
    private String role;
    private String username;
    private String email;
    private String nickname;
    private String introduce;
    private List<FileOnS3Dto> files;
    private String fileName;
    private String fileUrl;

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.role = user.getRole().getAuthority();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.files = user.getUserFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.fileName = files.size() > 0 ? files.get(0).getKeyName() : "image";
        this.fileUrl = files.size() > 0 ? files.get(0).getUploadFileUrl() : "https://vignette.wikia.nocookie.net/the-sun-vanished/images/5/5d/Twitter-avi-gender-balanced-figure.png/revision/latest?cb=20180713020754";
        this.introduce = user.getIntroduce() != null ? StringFormatter.format(user.getIntroduce()) : "";
    }
}
