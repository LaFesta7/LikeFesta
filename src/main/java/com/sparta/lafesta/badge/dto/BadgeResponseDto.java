package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BadgeResponseDto {
    private Long id;
    private String title;
    private String description;
    private BadgeConditionEnum conditionEnum;
    private LocalDate conditionFirstDay;
    private LocalDate conditionLastDay;
    private Long conditionStandard;

    public BadgeResponseDto(Badge badge) {
        this.id = badge.getId();
        this.title = badge.getTitle();
        this.description = badge.getDescription();
        this.conditionEnum = badge.getConditionEnum();
        this.conditionFirstDay = badge.getConditionFirstDay();
        this.conditionLastDay = badge.getConditionLastDay();
        this.conditionStandard = badge.getConditionStandard();
    }
}
