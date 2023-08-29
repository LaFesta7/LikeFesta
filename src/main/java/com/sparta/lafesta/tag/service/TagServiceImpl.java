package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.tag.repository.FestivalTagRepository;
import com.sparta.lafesta.tag.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final FestivalTagRepository festivalTagRepository;
  private final FestivalServiceImpl festivalService;

  //태그 생성
  @Override
  @Transactional
  public TagResponseDto createTag(TagRequestDto requestDto) {
    Tag tag = new Tag(requestDto);

    //태그 중복 방지
    Optional<Tag> checkTag = tagRepository.findByTitle(tag.getTitle());
    if (checkTag.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 태그입니다.");
    }

    tagRepository.save(tag);
    return new TagResponseDto(tag);
  }

  //태그 전체 조회
  @Override
  @Transactional(readOnly = true)
  public List<TagResponseDto> selectTags() {
    return tagRepository.findAllBy().stream()
        .map(TagResponseDto::new).toList();
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

  //페스티벌 태그 추가
  @Override
  @Transactional
  public ResponseEntity<ApiResponseDto> createFestivalTag(Long festivalId, Long tagId) {
    //태그를 추가할 페스티벌 조회
    Festival festival = festivalService.findFestival(festivalId);

    //추가할 태그 조회
    Tag tag = tagRepository.findById(tagId)
        .orElseThrow(() -> new IllegalArgumentException("해당 태그가 없습니다."));

    //중복 태그 예외 발생
    if (festivalTagRepository.findByTagAndFestival(tag, festival).isPresent()) {
      throw new IllegalArgumentException("이미 태그되었습니다.");
    }

    festivalTagRepository.save(new FestivalTag(tag, festival));

    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(),
        "태그 성공. 페스티벌 이름: '" + festival.getTitle() + "', 태그 이름: '" + tag.getTitle() + "'"));
  }

  //페스티벌 태그별 조회
  @Override
  @Transactional(readOnly = true)
  public List<FestivalResponseDto> selectFestivalTags(String title) {
    Tag tag = tagRepository.findByTitle(title)
        .orElseThrow(() -> new IllegalArgumentException("해당 태그가 없습니다."));

    List<FestivalTag> festivalTags = festivalTagRepository.findAllByTag(tag);

    List<FestivalResponseDto> tagedFestivals = new ArrayList<>();
    for (FestivalTag festivalTag : festivalTags) {
      Festival tagedFestival = festivalService.findFestivalByTag(festivalTag);

      FestivalResponseDto festivalResponseDto = new FestivalResponseDto(tagedFestival);
      tagedFestivals.add(festivalResponseDto);
    }
    return tagedFestivals;
  }

  //페스티벌 태그 삭제
  @Override
  @Transactional
  public ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId) {
    Festival festival = festivalService.findFestival(festivalId);

    Tag tag = tagRepository.findById(tagId)
        .orElseThrow(() -> new IllegalArgumentException("해당 태그가 없습니다."));

    //태그 관계 확인
    FestivalTag festivalTag = festivalTagRepository.findByTagAndFestival(tag, festival)
        .orElseThrow(() -> new IllegalArgumentException("태그 되어있지 않습니다."));

    festivalTagRepository.delete(festivalTag);

    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(),
        "'" + tag.getTitle() + "' 태그를 '" + festival.getTitle() + "'에서 제외했습니다."));
  }


  //태그 id로 태그 찾기
  private Tag findTag(Long tagId) {
    return tagRepository.findById(tagId).orElseThrow(() ->
        new IllegalArgumentException("선택한 태그는 존재하지 않습니다."));
  }
}
