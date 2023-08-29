package com.sparta.lafesta.festivalRequest.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FestivalRequestRequestDto {
    private String title;
    private String location;
    private String content;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String reservationPlace;
    private String officialLink;
}
