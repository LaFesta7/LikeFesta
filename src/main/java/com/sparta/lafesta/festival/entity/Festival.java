package com.sparta.lafesta.festival.entity;

import com.sparta.lafesta.common.entity.Timestamped;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Column(name = "reservation_place", nullable = false)
  private String reservationPlace;

  @Column(name = "official_link", nullable = false)
  private String officialLink;

  //연관관계

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;


  @OneToMany(mappedBy = "festival", orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  @OneToMany(mappedBy = "followedFestival", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<FestivalFollow> festivalFollowers = new ArrayList<>();

  @OneToMany(mappedBy = "festival", orphanRemoval = true)
  private List<FestivalLike> festivalLikes = new ArrayList<>();

  @OneToMany(mappedBy = "festival", orphanRemoval = true) //todo 이후 프론트 전달 방식과 관련해서 개선필요해 보임
  private List<FestivalFileOnS3> festivalFileOnS3s = new ArrayList<>();

  @OneToMany(mappedBy = "festival", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<FestivalTag> tags = new ArrayList<>();

  ////생성자 - 약속된 형태로만 생성가능하도록 합니다.

  public Festival(FestivalRequestDto requestDto, User user) {

    this.title = requestDto.getTitle();
    this.location = requestDto.getLocation();
    this.content = requestDto.getContent();
    this.openDate = requestDto.getOpenDate();
    this.endDate = requestDto.getEndDate();
    this.reservationOpenDate = requestDto.getReservationOpenDate();
    this.reservationPlace = requestDto.getReservationPlace();
    this.officialLink = requestDto.getOfficialLink();
    this.user = user;
  }

  //// 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)

  public void modify(FestivalRequestDto requestDto) {
    this.title = requestDto.getTitle();
    this.location = requestDto.getLocation();
    this.content = requestDto.getContent();
    this.openDate = requestDto.getOpenDate();
    this.endDate = requestDto.getEndDate();
    this.reservationOpenDate = requestDto.getReservationOpenDate();
    this.reservationPlace = requestDto.getReservationPlace();
    this.officialLink = requestDto.getOfficialLink();
  }

}
