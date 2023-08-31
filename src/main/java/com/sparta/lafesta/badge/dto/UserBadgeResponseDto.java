package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.UserBadge;
import lombok.Getter;

@Getter
public class UserBadgeResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean representative;

    public UserBadgeResponseDto(UserBadge userBadge) {
        this.id = userBadge.getBadge().getId();
        this.title = userBadge.getBadge().getTitle();
        this.description = userBadge.getBadge().getDescription();
        this.representative = userBadge.getRepresentative();
    }
}
