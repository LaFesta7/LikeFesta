package com.sparta.lafesta.festivalRequest.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festivalRequest.dto.FestivaRequestlResponseDto;
import com.sparta.lafesta.festivalRequest.dto.FestivalRequestRequestDto;
import com.sparta.lafesta.festivalRequest.service.FestivalRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "페스티벌 게시 요청 관련 API", description = "페스티벌 게시 요청 관련 API 입니다.")
public class FestivalRequestController {
    private final FestivalRequestService festivalRequestService;

    @PostMapping("/festival-requests")
    @Operation(summary = "페스티벌 게시 요청 등록", description = "페스티벌을 생성합니다. Dto를 통해 정보를 받아와 festival을 생성할 때 해당 정보를 저장합니다.")
    public ResponseEntity<ApiResponseDto> createFestival(
            @Parameter(description = "festival 요청을 생성할 때 필요한 정보") @RequestBody FestivalRequestRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivaRequestlResponseDto result = festivalRequestService.createFestivalRequest(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), result.getTitle()+" 요청을 추가했습니다."));
    }

    @GetMapping("/festival-requests")
    @Operation(summary = "페스티벌 게시 요청 전체 조회", description = "페스티벌 게시 요청을 전체 조회합니다.")
    public ResponseEntity<List<FestivaRequestlResponseDto>> selectFestivalRequests() {
        List<FestivaRequestlResponseDto> results = festivalRequestService.selectFestivalRequests();
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/festival-requests/{festivalRequestId}")
    @Operation(summary = "페스티벌 게시 요청 상세 조회", description = "@PathVariable을 통해 festivalRequest Id를 받아와, 해당 festival 게시 요청을 상세 조회합니다.")
    public ResponseEntity<FestivaRequestlResponseDto> selectFestivalRequest(
            @Parameter(name = "festivalRequestId", description = "선택한 festivalRequest의 id", in = ParameterIn.PATH) @PathVariable Long festivalRequestId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivaRequestlResponseDto result = festivalRequestService.selectFestivalRequest(festivalRequestId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/festival-requests/{festivalRequestId}")
    @Operation(summary = "페스티벌 게시 요청 내용 수정", description = "@PathVariable을 통해 festivalRequest Id를 받아와, 해당 페스티벌 게시 요청의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<FestivaRequestlResponseDto> modifyFestivalRequest(
            @Parameter(name = "festivalRequestId", description = "수정할 festival 게시 요청글의 id", in = ParameterIn.PATH) @PathVariable Long festivalRequestId,
            @Parameter(description = "festivalRequest를 수정할 때 필요한 정보") @RequestBody FestivalRequestRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FestivaRequestlResponseDto result = festivalRequestService.modifyFestivalRequest(festivalRequestId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/festival-requests/{festivalRequestId}")
    @Operation(summary = "페스티벌 게시 요청 삭제", description = "@PathVariable을 통해 festivalRequestId를 받아와, 해당 페스티벌 게시 요청을 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteFestival(
            @Parameter(name = "festivalRequestId", description = "삭제할 페스티벌 게시 요청글의 id", in = ParameterIn.PATH) @PathVariable Long festivalRequestId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        festivalRequestService.deleteFestivalRequest(festivalRequestId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "페스티벌 게시 요청 삭제 완료"));
    }
}
