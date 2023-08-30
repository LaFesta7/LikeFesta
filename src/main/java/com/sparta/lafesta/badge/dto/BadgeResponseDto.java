package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class BadgeResponseDto {
    private Long id;
    private String title;
    private String description;
    private BadgeConditionEnum conditionEnum;
    private LocalDate conditionFirstDay;
    private LocalDate conditionLastDay;
    private int conditionStandard;
    private List<FileOnS3Dto> files;

    public BadgeResponseDto(Badge badge) {
        this.id = badge.getId();
        this.title = badge.getTitle();
        this.description = badge.getDescription();
        this.conditionEnum = badge.getConditionEnum();
        this.conditionFirstDay = badge.getConditionFirstDay();
        this.conditionLastDay = badge.getConditionLastDay();
        this.conditionStandard = badge.getConditionStandard();
        this.files = badge.getBadgeFileOnS3s().stream().map(FileOnS3Dto::new).toList();
    }
}
