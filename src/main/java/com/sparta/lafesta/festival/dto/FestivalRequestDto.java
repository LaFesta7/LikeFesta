package com.sparta.lafesta.festival.dto;

import com.sparta.lafesta.tag.dto.TagRequestDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class FestivalRequestDto {

  private String title;
  private String location;
  private String content;

  //예매 오픈 시각
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime reservationOpenDate;

  // 예매처
  private String reservationPlace;

  //행사 시작 시각
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime openDate;

  //행사 종료 시각
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endDate;


  private String officialLink;

  private List<TagRequestDto> tagList = new ArrayList<>();
}
