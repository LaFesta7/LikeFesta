package com.sparta.lafesta.festivalRequest.dto;


import com.sparta.lafesta.festivalRequest.entity.FestivalRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FestivaRequestlResponseDto {
    private Long id;
    private String title;
    private String location;
    private String content;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String officialLink;
    private Boolean adminApproval;
    private String nickname;

    public FestivaRequestlResponseDto(FestivalRequest festivalRequest) {
        this.id = festivalRequest.getId();
        this.title = festivalRequest.getTitle();
        this.location = festivalRequest.getLocation();
        this.content = festivalRequest.getContent();
        this.openDate = festivalRequest.getOpenDate();
        this.endDate = festivalRequest.getEndDate();
        this.reservationOpenDate = festivalRequest.getReservationOpenDate();
        this.officialLink = festivalRequest.getOfficialLink();
        this.adminApproval = festivalRequest.getAdminApproval();
        this.nickname = festivalRequest.getUser().getNickname();
    }
}
