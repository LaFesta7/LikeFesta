package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserBadgeResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean representative;
    private String fileUrl;

    public UserBadgeResponseDto(UserBadge userBadge) {
        this.id = userBadge.getBadge().getId();
        this.title = userBadge.getBadge().getTitle();
        this.description = userBadge.getBadge().getDescription();
        this.representative = userBadge.getRepresentative();
        this.fileUrl = userBadge.getBadge().getBadgeFileOnS3s().get(0).getUploadFileUrl();
    }
}
