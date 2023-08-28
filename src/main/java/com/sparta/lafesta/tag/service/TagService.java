package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import java.util.List;

public interface TagService {

  TagResponseDto createTag(TagRequestDto requestDto);

  List<TagResponseDto> selectTags();

  TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto);

  void deleteTag(Long tagId);

//  TagResponseDto createFestivalTag(TagRequestDto requestDto);
//
//  List<TagResponseDto> selectFestivalTags(TagRequestDto requestDto);
//
//  TagResponseDto modifyFestivalTag(TagRequestDto requestDto);
//
//  void deleteFestivalTag(TagRequestDto requestDto);
}
