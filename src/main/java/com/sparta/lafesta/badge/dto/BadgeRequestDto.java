package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class BadgeRequestDto {
    private String title;
    private String description;
    private BadgeConditionEnum conditionEnum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate conditionFirstDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate conditionLastDay;

    private Long conditionStandard;
}
