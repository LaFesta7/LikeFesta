package com.sparta.lafesta.festival.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FestivalRequestDto {
    private String title;
    private String location;
    private String content;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String officialLink;
}
