package com.sparta.lafesta.festival.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "festivals")
public class Festival extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "open_date", nullable = false)
    private LocalDateTime openDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "reservation_open_date", nullable = false)
    private LocalDateTime reservationOpenDate;

    @Column(name = "official_link", nullable = false)
    private String officialLink;

    @OneToMany(mappedBy = "festival", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "festival", orphanRemoval = true)
    private List<FestivalLike> festivalLikes = new ArrayList<>();

    public Festival(FestivalRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.location = requestDto.getLocation();
        this.content = requestDto.getContent();
        this.openDate = requestDto.getOpenDate();
        this.endDate = requestDto.getEndDate();
        this.reservationOpenDate = requestDto.getReservationOpenDate();
        this.officialLink = requestDto.getOfficialLink();
    }

    public void modify(FestivalRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.location = requestDto.getLocation();
        this.content = requestDto.getContent();
        this.openDate = requestDto.getOpenDate();
        this.endDate = requestDto.getEndDate();
        this.reservationOpenDate = requestDto.getReservationOpenDate();
        this.officialLink = requestDto.getOfficialLink();
    }
}
