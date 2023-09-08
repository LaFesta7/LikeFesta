package com.sparta.lafesta.badge.controller;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.service.BadgeService;
import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "뱃지 관련 API", description = "뱃지 관련 API 입니다.")
public class BadgeController {
    private final BadgeService badgeService;

    @PostMapping("/admin/badges")
    @Operation(summary = "뱃지 생성", description = "Dto를 통해 정보를 받아와 뱃지를 생성합니다.")
    public ResponseEntity<ApiResponseDto> createBadge (
            @Parameter(description = "뱃지를 생성할 때 필요한 정보") @RequestPart BadgeRequestDto requestDto,
            @Parameter(description = "festival 생성시 등록한 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
            ) throws IOException {
            BadgeResponseDto result = badgeService.createBadge(requestDto, files, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "뱃지를 생성했습니다."));
    }

    @GetMapping("/admin/badges")
    @Operation(summary = "뱃지 전체 조회", description = "생성된 뱃지를 모두 조회합니다.")
    public ResponseEntity<List<BadgeResponseDto>> selectBadges (
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "뱃지 페이지 처리에 필요한 기본 설정")
        @PageableDefault(size=20, sort="id") Pageable pageable
    ) {
        List<BadgeResponseDto> results = badgeService.selectBadges(userDetails.getUser(), pageable);
        return ResponseEntity.ok().body(results);
    }

    @PatchMapping("/admin/badges/{badgeId}")
    @Operation(summary = "뱃지 수정", description = "@PathVariable을 통해 badgeId를 받아와, 해당 뱃지를 수정합니다. Dto를 통해 정보를 받아옵니다.")
    public ResponseEntity<BadgeResponseDto> modifyBadge (
            @Parameter(name = "badgeId", description = "수정할 badge의 id", in = ParameterIn.PATH) @PathVariable Long badgeId,
            @Parameter(description = "뱃지를 수정할 때 필요한 정보") @RequestPart BadgeRequestDto requestDto,
            @Parameter(description = "festival 수정시 등록한 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        BadgeResponseDto result = badgeService.modifyBadge(badgeId, requestDto, files, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/admin/badges/{badgeId}")
    @Operation(summary = "뱃지 삭제", description = "@PathVariable을 통해 badgeId를 받아와, 해당 뱃지를 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteBadge (
            @Parameter(name = "badgeId", description = "삭제할 badge의 id", in = ParameterIn.PATH) @PathVariable Long badgeId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        badgeService.deleteBadge(badgeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "뱃지를 삭제했습니다."));
    }

    @GetMapping("/users/{userId}/badges")
    @Operation(summary = "유저 뱃지 보유 목록 조회", description = "userId를 받아 해당 유저가 보유한 모든 뱃지를 조회합니다.")
    public ResponseEntity<List<UserBadgeResponseDto>> selectUserBadges (
            @Parameter(name = "userId", description = "조회할 user의 id", in = ParameterIn.PATH) @PathVariable Long userId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "유저 뱃지 페이지 처리에 필요한 기본 설정")
            @PageableDefault(size=20, sort="createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        List<UserBadgeResponseDto> results = badgeService.selectUserBadges(userId, userDetails.getUser(), pageable);
        return ResponseEntity.ok().body(results);
    }

    @PatchMapping("/users/{userId}/badges/{badgeId}")
    @Operation(summary = "유저 대표 뱃지 설정/해제", description = "userId, badgeId를 받아 해당 유저가 보유한 뱃지 중 다른 사람에게 노출할 대표 뱃지를 설정/해제합니다.")
    public ResponseEntity<UserBadgeResponseDto> modifyUserBadgeRepresentative (
            @Parameter(name = "userId", description = "설정할 user의 id", in = ParameterIn.PATH) @PathVariable Long userId,
            @Parameter(name = "badgeId", description = "설정할 badge의 id", in = ParameterIn.PATH) @PathVariable Long badgeId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserBadgeResponseDto result = badgeService.modifyUserBadgeRepresentative(userId, badgeId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }
}
