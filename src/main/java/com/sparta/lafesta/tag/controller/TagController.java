package com.sparta.lafesta.tag.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.service.TagServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "태그 관련 API", description = "태그 관련 API입니다.")
public class TagController {

  private final TagServiceImpl tagservice;

  @PostMapping("/tags")
  public ResponseEntity<ApiResponseDto> createTag(
      @RequestBody TagRequestDto requestDto
  ) {
    TagResponseDto result = tagservice.createTag(requestDto);
    return ResponseEntity.ok()
        .body(new ApiResponseDto(HttpStatus.CREATED.value(), result.getTitle() + "를 추가했습니다."));
  }

  @GetMapping("/tags")
  public ResponseEntity<List<TagResponseDto>> selectTags() {
    List<TagResponseDto> results = tagservice.selectTags();
    return ResponseEntity.ok().body(results);
  }

  @PatchMapping("/tags/{tagId}")
  public ResponseEntity<TagResponseDto> modifyTag(
      @PathVariable Long tagId,
      @RequestBody TagRequestDto requestDto
  ) {
    TagResponseDto result = tagservice.modifyTag(tagId, requestDto);
    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping("/tags/{tagId}")
  public ResponseEntity<ApiResponseDto> deleteTag(
      @PathVariable Long tagId
  ) {
    tagservice.deleteTag(tagId);
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "태그 삭제 완료"));
  }

  @PostMapping("/festivals/{festivalId}/tags")
  public void createFestivalTag() {

  }

  @GetMapping("/festivals/tags")
  public void selectFestivalTags(@RequestParam("tag") String tag) {

  }

  @PatchMapping("/festivals/{festivalId}/tags/{tagId}")
  public void modifyFestivalTag() {

  }

  @DeleteMapping("/festivals/{festivalId}/tags/{tagId}")
  public void deleteFetivalTag() {

  }
}
