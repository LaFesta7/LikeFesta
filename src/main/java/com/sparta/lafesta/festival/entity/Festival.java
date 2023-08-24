package com.sparta.lafesta.festival.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "festival", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "followedFestival", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FestivalFollow> festivalFollowers = new ArrayList<>();

    @OneToMany(mappedBy = "festival", orphanRemoval = true)
    private List<FestivalLike> festivalLikes = new ArrayList<>();

    public Festival(FestivalRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.location = requestDto.getLocation();
        this.content = requestDto.getContent();
        this.openDate = requestDto.getOpenDate();
        this.endDate = requestDto.getEndDate();
        this.reservationOpenDate = requestDto.getReservationOpenDate();
        this.officialLink = requestDto.getOfficialLink();
        this.user = user;
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
