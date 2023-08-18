package com.sparta.lafesta.festival.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.entity.Review;
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
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String officialLink;
    private List<Review> reviews;

    public FestivalResponseDto(Festival festival) {
        this.id = festival.getId();
        this.title = festival.getTitle();
        this.location = festival.getLocation();
        this.content = festival.getContent();
        this.openDate = festival.getOpenDate();
        this.endDate = festival.getEndDate();
        this.reservationOpenDate = festival.getReservationOpenDate();
        this.officialLink = festival.getOfficialLink();
        this.reviews = festival.getReviews().stream().
                map(ReviewResponseDto::new).collect(Collectors.toList());
    }
}
