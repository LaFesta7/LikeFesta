package com.sparta.lafesta.badge.dto;

import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
public class BadgeRequestDto {
    private String title;
    private String description;
    private BadgeConditionEnum conditionEnum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate conditionFirstDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate conditionLastDay;

    private int conditionStandard;

    private List<TagRequestDto> conditionTags;
}
