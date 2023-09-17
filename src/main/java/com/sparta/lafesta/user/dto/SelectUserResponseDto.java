package com.sparta.lafesta.user.dto;

import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.common.exception.NotFoundException;
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
    private String introduce;
    private UserRoleEnum role;
    private List<FileOnS3Dto> files;
    private String fileName;
    private String fileUrl;
    private List<UserBadgeResponseDto> representativeBadges;

    public SelectUserResponseDto(User user){
        if (user == null) {
            throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.role = user.getRole();
        this.files = user.getUserFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.fileName = files.size() > 0 ? files.get(0).getKeyName() : "image";
        this.fileUrl = files.size() > 0 ? files.get(0).getUploadFileUrl() : "https://vignette.wikia.nocookie.net/the-sun-vanished/images/5/5d/Twitter-avi-gender-balanced-figure.png/revision/latest?cb=20180713020754";
        this.representativeBadges = user.getUserBadges().stream()
                .filter(UserBadge::isRepresentative).map(UserBadgeResponseDto::new).toList();
    }
}
