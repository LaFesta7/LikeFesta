package com.sparta.lafesta.festivalRequest.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.festivalRequest.dto.FestivalRequestRequestDto;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "festivalRequest")
public class FestivalRequest extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "open_date", nullable = false)
    private LocalDateTime openDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "reservation_open_date", nullable = false)
    private LocalDateTime reservationOpenDate;

    @Column(name = "reservation_place", nullable = false)
    private String reservationPlace;

    @Column(name = "official_link", nullable = false)
    private String officialLink;

    @Column(name = "admin_approval", nullable = false)
    private Boolean adminApproval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public FestivalRequest(FestivalRequestRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.place = requestDto.getPlace();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.content = requestDto.getContent();
        this.openDate = requestDto.getOpenDate();
        this.endDate = requestDto.getEndDate();
        this.reservationOpenDate = requestDto.getReservationOpenDate();
        this.reservationPlace = requestDto.getReservationPlace();
        this.officialLink = requestDto.getOfficialLink();
        this.adminApproval = false;
        this.user = user;
    }

    public void modify(FestivalRequestRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.place = requestDto.getPlace();
        this.content = requestDto.getContent();
        this.openDate = requestDto.getOpenDate();
        this.endDate = requestDto.getEndDate();
        this.reservationOpenDate = requestDto.getReservationOpenDate();
        this.reservationPlace = requestDto.getReservationPlace();
        this.officialLink = requestDto.getOfficialLink();
    }

    public void approveFestivalRequest() {
        this.adminApproval = true;
    }
}
