package com.sparta.lafesta.festival.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class FestivalRequestDto {
    private String title;
    private String location;
    private String content;

    //예매 오픈 시각
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationOpenDate;

    //행사 시작 시각
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime openDate;

    //행사 종료 시각
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;



    private String officialLink;
}
