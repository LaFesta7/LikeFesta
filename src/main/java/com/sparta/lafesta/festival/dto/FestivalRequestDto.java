package com.sparta.lafesta.festival.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
