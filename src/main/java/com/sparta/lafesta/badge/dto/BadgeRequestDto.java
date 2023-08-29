package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class BadgeRequestDto {
    private String title;
    private String description;
    private BadgeConditionEnum cndtn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDay;

    private Long standard;
}
