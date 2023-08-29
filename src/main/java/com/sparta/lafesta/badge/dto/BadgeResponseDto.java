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
    private BadgeConditionEnum cndtn;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private Long standard;

    public BadgeResponseDto(Badge badge) {
        this.id = badge.getId();
        this.title = badge.getTitle();
        this.description = badge.getDescription();
        this.cndtn = badge.getCndtn();
        this.firstDay = badge.getFirstDay();
        this.lastDay = badge.getLastDay();
        this.standard = badge.getStandard();
    }
}
