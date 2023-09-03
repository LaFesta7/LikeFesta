package com.sparta.lafesta.festival.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.service.FestivalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "페스티벌 관련 API", description = "페스티벌 관련 API 입니다.")
public class FestivalController {
    private final FestivalService festivalService;

    @PostMapping("/festivals")
    @Operation(summary = "페스티벌 등록", description = "페스티벌을 생성합니다. Dto를 통해 정보를 받아와 festival을 생성할 때 해당 정보를 저장합니다.")
    public ResponseEntity<ApiResponseDto> createFestival(
            @Parameter(description = "festival을 생성할 때 필요한 정보") @RequestPart(value = "requestDto") FestivalRequestDto requestDto,
            @Parameter(description = "festival 생성시 등록한 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {

        FestivalResponseDto result = festivalService.createFestival(requestDto, files, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), result.getTitle()+"를 추가했습니다."));
    }

    @GetMapping("/festivals")
    @Operation(summary = "페스티벌 전체 조회", description = "페스티벌을 전체 조회합니다.")
    public Object selectFestivals(
            @Parameter(description = "축제 페이지 처리에 필요한 기본 설정")
            @PageableDefault(size=10, sort="createdAt", direction = Direction.DESC) Pageable pageable,
            @RequestParam(value = "apiMode", required = false) Boolean apiMode,
            Model model
    ) {
        List<FestivalResponseDto> results = festivalService.selectFestivals(pageable);

        if (Boolean.TRUE.equals(apiMode)) {
            // apiMode가 true이면 ResponseEntity를 반환합니다.
            return ResponseEntity.ok().body(results);
        } else {
            // 그렇지 않으면 모델을 채우고 뷰 이름을 반환합니다.
            model.addAttribute("festivals", results);
            return "festivalListPage";
        }
    }

    @GetMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 상세 조회", description = "@PathVariable을 통해 festivalId 받아와, 해당 festival을 상세 조회합니다.")
    public ResponseEntity<FestivalResponseDto> selectFestival(
            @Parameter(name = "festivalId", description = "선택한 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.selectFestival(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 내용 수정", description = "@PathVariable을 통해 festival Id를 받아와, 해당 페스티벌의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<FestivalResponseDto> modifyFestival(
            @Parameter(name = "festivalId", description = "수정할 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "festival을 수정할 때 필요한 정보") @RequestPart(value = "requestDto") FestivalRequestDto requestDto,
            @Parameter(description = "festival 생성시 등록한 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        FestivalResponseDto result = festivalService.modifyFestival(festivalId, requestDto, files, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/festivals/{festivalId}")
    @Operation(summary = "페스티벌 삭제", description = "@PathVariable을 통해 festivalId를 받아와, 해당 페스티벌을 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteFestival(
            @Parameter(name = "festivalId", description = "삭제할 페스티벌의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        festivalService.deleteFestival(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "페스티벌 삭제 완료"));
    }

    @PostMapping("/festivals/{festivalId}/likes")
    @Operation(summary = "페스티벌 좋아요 추가", description = "페스티벌에 좋아요를 추가합니다.")
    public ResponseEntity<ApiResponseDto> createFestivalLike(
            @Parameter(name = "festivalId", description = "좋아요를 추가할 페스티벌의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.createFestivalLike(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "좋아요를 추가했습니다. 좋아요 수: " + result.getLikeCnt()));
    }

    @DeleteMapping("/festivals/{festivalId}/likes-cancel")
    @Operation(summary = "페스티벌 좋아요 취소", description = "페스티벌에 좋아요를 취소합니다.")
    public ResponseEntity<ApiResponseDto> deleteFestivalLike(
            @Parameter(name = "festivalId", description = "좋아요를 취소할 페스티벌의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivalResponseDto result = festivalService.deleteFestivalLike(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "좋아요를 취소했습니다. 좋아요 수: " + result.getLikeCnt()));
    }

    @GetMapping("/festivals/rank")
    @Operation(summary = "페스티벌 랭킹 조회", description = "페스티벌 중 가장 리뷰 수가 많은 TOP3를 조회합니다.")
    public ResponseEntity<List<FestivalResponseDto>> selectFestivalRanking(
        @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<FestivalResponseDto> results = festivalService.selectFestivalRanking(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }
}
