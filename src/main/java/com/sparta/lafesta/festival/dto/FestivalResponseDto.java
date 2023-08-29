package com.sparta.lafesta.festival.dto;

import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
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
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String reservationPlace;
    private String officialLink;
    private List<ReviewResponseDto> reviews;
    private List<FileOnS3Dto> files;
    private int likeCnt;

    public FestivalResponseDto(Festival festival) {
        this.id = festival.getId();
        this.title = festival.getTitle();
        this.location = festival.getLocation();
        this.content = festival.getContent();
        this.openDate = festival.getOpenDate();
        this.endDate = festival.getEndDate();
        this.reservationOpenDate = festival.getReservationOpenDate();
        this.reservationPlace = festival.getReservationPlace();
        this.officialLink = festival.getOfficialLink();
        this.reviews = festival.getReviews().stream().
                map(ReviewResponseDto::new).collect(Collectors.toList());
        this.files = festival.getFestivalFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.likeCnt = festival.getFestivalLikes().size();
    }
}
