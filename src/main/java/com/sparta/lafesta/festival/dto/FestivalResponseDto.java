package com.sparta.lafesta.festival.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FestivalResponseDto {
    private Long id;
    private String title;
    private String location;
    private String content;
    private LocalDateTime reservationOpenDate;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private String officialLink;
    private List<ReviewResponseDto> reviews;
    private int likeCnt;

    public FestivalResponseDto(Festival festival) {
        this.id = festival.getId();
        this.title = festival.getTitle();
        this.location = festival.getLocation();
        this.content = festival.getContent();
        this.reservationOpenDate = festival.getReservationOpenDate();
        this.openDate = festival.getOpenDate();
        this.endDate = festival.getEndDate();
        this.officialLink = festival.getOfficialLink();
        this.reviews = festival.getReviews().stream().
                map(ReviewResponseDto::new).collect(Collectors.toList());
        this.likeCnt = festival.getFestivalLikes().size();
    }
}
