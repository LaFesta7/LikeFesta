package com.sparta.lafesta.tag.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.service.TagServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/tags")
    @Operation(summary = "전체 태그 조회", description = "태그를 전체 조회합니다.")
    public ResponseEntity<Page<TagResponseDto>> selectTags(
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "페이지 처리에 필요한 기본 설정")
            @PageableDefault(size=10, sort="title", direction = Direction.ASC) Pageable pageable
    ) {
        Page<TagResponseDto> results = tagservice.selectTags(userDetails.getUser(), pageable);
        return ResponseEntity.ok().body(results);
    }

    @PatchMapping("/tags/{tagId}")
    @Operation(summary = "태그 수정", description = "@PathVariable을 통해 tagId를 받아와, 해당 태그의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<TagResponseDto> modifyTag(
            @Parameter(name = "tagId", description = "수정할 tag의 id", in = ParameterIn.PATH) @PathVariable Long tagId,
            @Parameter(description = "태그를 수정할 때 필요한 정보") @RequestBody TagRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        TagResponseDto result = tagservice.modifyTag(tagId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/tags/{tagId}")
    @Operation(summary = "태그 삭제", description = "@PathVariable을 통해 tagId를 받아와, 해당 태그를 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteTag(
            @Parameter(name = "tagId", description = "삭제할 tag의 id", in = ParameterIn.PATH) @PathVariable Long tagId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tagservice.deleteTag(tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "태그 삭제 완료"));
    }

    @GetMapping("/festivals/tags")
    @Operation(summary = "페스티벌 태그별 조회", description = "@RequestParam을 통해 태그의 title을 받아와, 해당 태그를 사용하는 페스티벌을 전체 조회합니다.")
    public ResponseEntity<List<FestivalResponseDto>> selectFestivalTags(
            @Parameter(description = "페스티벌을 조회할 때 사용할 태그 이름") @RequestParam("tag") String tag,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "페이지 처리에 필요한 기본 설정")
            @PageableDefault(size=10, sort="id", direction = Direction.DESC) Pageable pageable
    ) {
        List<FestivalResponseDto> results = tagservice.selectFestivalTags(tag, userDetails.getUser(), pageable);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/festivals/{festivalId}/tags/{tagId}")
    @Operation(summary = "페스티벌 태그 삭제", description = "@PathVariable을 통해 festivalId와 tagId를 받아와, 페스티벌과 태그 간의 연관관계를 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteFetivalTag(
            @Parameter(name = "festivalId", description = "tag를 삭제할 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(name = "tagId", description = "삭제될 tag의 id", in = ParameterIn.PATH) @PathVariable Long tagId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return tagservice.deleteFestivalTag(festivalId, tagId, userDetails.getUser());
    }
}
