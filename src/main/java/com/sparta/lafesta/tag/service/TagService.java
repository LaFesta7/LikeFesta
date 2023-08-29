package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface TagService {

  TagResponseDto createTag(TagRequestDto requestDto);

  List<TagResponseDto> selectTags();

  TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto);

  void deleteTag(Long tagId);

  ResponseEntity<ApiResponseDto> createFestivalTag(Long festivalId, Long tagId);

  List<FestivalResponseDto> selectFestivalTags(String tag);

//  TagResponseDto modifyFestivalTag(TagRequestDto requestDto);

  ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId);
}
