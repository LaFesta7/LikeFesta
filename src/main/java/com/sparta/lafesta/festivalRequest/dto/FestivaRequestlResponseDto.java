package com.sparta.lafesta.festivalRequest.dto;


import com.sparta.lafesta.common.entity.StringFormatter;
import com.sparta.lafesta.festivalRequest.entity.FestivalRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FestivaRequestlResponseDto {
    private Long id;
    private String title;
    private String place;
    private String content;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime reservationOpenDate;
    private String reservationPlace;
    private String officialLink;
    private Boolean adminApproval;
    private String nickname;

    public FestivaRequestlResponseDto(FestivalRequest festivalRequest) {
        this.id = festivalRequest.getId();
        this.title = StringFormatter.format(festivalRequest.getTitle());
        this.place = StringFormatter.format(festivalRequest.getPlace());
        this.content = StringFormatter.format(festivalRequest.getContent());
        this.openDate = festivalRequest.getOpenDate();
        this.endDate = festivalRequest.getEndDate();
        this.reservationOpenDate = festivalRequest.getReservationOpenDate();
        this.reservationPlace = festivalRequest.getReservationPlace() != null ? StringFormatter.format(festivalRequest.getReservationPlace()) : "";
        this.officialLink = festivalRequest.getOfficialLink();
        this.adminApproval = festivalRequest.getAdminApproval();
        this.nickname = festivalRequest.getUser().getNickname();
    }
}
