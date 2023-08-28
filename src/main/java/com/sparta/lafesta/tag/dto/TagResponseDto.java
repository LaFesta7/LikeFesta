package com.sparta.lafesta.tag.dto;

import com.sparta.lafesta.tag.entity.Tag;
import lombok.Getter;

@Getter
public class TagResponseDto {

  private Long id;
  private String title;

  public TagResponseDto(Tag tag) {
    this.id = tag.getId();
    this.title = tag.getTitle();
  }
}
