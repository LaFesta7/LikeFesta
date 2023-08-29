package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface TagService {

  /**
   * 태그 생성
   *
   * @param requestDto 생성할 태그의 정보
   * @return 태그 추가 결과
   */
  TagResponseDto createTag(TagRequestDto requestDto);

  /**
   * 전체 태그 조회
   *
   * @return 전체 태그 조회 결과
   */
  List<TagResponseDto> selectTags();

  /**
   * 태그 수정
   *
   * @param tagId      수정할 태그의 id
   * @param requestDto 수정할 태그의 정보
   * @return 태그 수정 결과
   */
  TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto);

  /**
   * 태그 삭제
   *
   * @param tagId 삭제할 태그의 id
   */
  void deleteTag(Long tagId);

  /**
   * 페스티벌 태그 추가
   *
   * @param festivalId 태그를 추가할 페스티벌의 정보
   * @param tagId      추가될 태그의 정보
   * @return 페스티벌 태그 추가 결과
   */
  ResponseEntity<ApiResponseDto> createFestivalTag(Long festivalId, Long tagId);

  /**
   * 페스티벌 태그별 조회
   *
   * @param tag 조회할 태그의 정보
   * @return 태그별 페스티벌 조회 결과
   */
  List<FestivalResponseDto> selectFestivalTags(String tag);

  /**
   * 페스티벌 태그 삭제
   *
   * @param festivalId 태그를 삭제할 페스티벌의 id
   * @param tagId      페스티벌에서 제거될 태그의 id
   * @return 태그 삭제 결과
   */
  ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId);
}
