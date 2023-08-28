package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.tag.repository.FestivalTagRepository;
import com.sparta.lafesta.tag.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final FestivalTagRepository festivalTagRepository;

  //태그 생성
  @Override
  @Transactional
  public TagResponseDto createTag(TagRequestDto requestDto) {
    Tag tag = new Tag(requestDto);
    tagRepository.save(tag);
    return new TagResponseDto(tag);
  }

  //태그 전체 조회
  @Override
  @Transactional(readOnly = true)
  public List<TagResponseDto> selectTags() {
    return tagRepository.findAllBy().stream()
        .map(TagResponseDto::new).collect(Collectors.toList());
  }

  //태그 수정
  @Override
  @Transactional
  public TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto) {
    Tag tag = findTag(tagId);

    tag.modify(requestDto);
    return new TagResponseDto(tag);
  }

  //태그 삭제
  @Override
  @Transactional
  public void deleteTag(Long tagId) {
    Tag tag = findTag(tagId);

    tagRepository.delete(tag);
  }

//  //페스티벌 태그 추가
//  @Override
//  public TagResponseDto createFestivalTag(TagRequestDto requestDto) {
//
//  }
//
//  //페스티벌 태그별 조회
//  @Override
//  public List<TagResponseDto> selectFestivalTags(TagRequestDto requestDto) {
//
//  }
//
//  //페스티벌 태그 수정
//  @Override
//  public TagResponseDto modifyFestivalTag(TagRequestDto requestDto) {
//
//  }
//
//  //페스티벌 태그 삭제
//  @Override
//  public void deleteFestivalTag(TagRequestDto requestDto) {
//
//  }

  //태그 id로 태그 찾기
  private Tag findTag(Long tagId) {
    return tagRepository.findById(tagId).orElseThrow(() ->
        new IllegalArgumentException("선택한 태그는 존재하지 않습니다."));
  }
}
