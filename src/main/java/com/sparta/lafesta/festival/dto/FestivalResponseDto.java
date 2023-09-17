package com.sparta.lafesta.festival.dto;

import com.sparta.lafesta.common.s3.dto.FileOnS3Dto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.entity.FestivalTag;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FestivalResponseDto {
    private Long id;
    private String title;
    private String place;
    private Double latitude;
    private Double longitude;
    private String content;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String reservationPlace;
    private String officialLink;
    private List<ReviewResponseDto> reviews;
    private List<FileOnS3Dto> files;
    private String fileName;
    private String fileUrl;
    private List<TagResponseDto> tags;
    private int likeCnt;
    private Long editorId;
    private String editorName;

    public FestivalResponseDto(Festival festival) {
        this.id = festival.getId();
        this.title = festival.getTitle();
        this.place = festival.getPlace();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
        this.content = festival.getContent();
        this.openDate = festival.getOpenDate();
        this.endDate = festival.getEndDate();
        this.reservationOpenDate = festival.getReservationOpenDate();
        this.reservationPlace = festival.getReservationPlace();
        this.officialLink = festival.getOfficialLink();
        this.reviews = festival.getReviews().stream().
                map(ReviewResponseDto::new).toList();
        this.files = festival.getFestivalFileOnS3s().stream().
                map(FileOnS3Dto::new).toList();
        this.fileName = files.size() > 0 ? files.get(0).getKeyName() : "image";
        this.fileUrl = files.size() > 0 ? files.get(0).getUploadFileUrl() : "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FxGuK9%2FbtsufX7IOe1%2FdJbJpCZ5UM6CYK5vGkS8Tk%2Fimg.png";
        this.tags = festival.getTags().stream()
                .map(FestivalTag::getTag).map(TagResponseDto::new).toList();
        this.likeCnt = festival.getFestivalLikes().size();
        this.editorId = festival.getUser().getId();
        this.editorName = festival.getUser().getUsername();
    }
}
