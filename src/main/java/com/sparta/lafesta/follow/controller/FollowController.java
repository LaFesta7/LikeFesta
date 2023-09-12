package com.sparta.lafesta.follow.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.follow.service.FollowService;
import com.sparta.lafesta.user.dto.SelectUserResponseDto;
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

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "팔로우 관련 API", description = "팔로우 관련 API 입니다.")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/users/follows/{followingUserId}")
    @Operation(summary = "유저 팔로우 추가", description = "@PathVariable을 통해 followingUserId를 받아와, 해당 User를 팔로우합니다")
    public ResponseEntity<ApiResponseDto> createFollowingUser(
            @Parameter(description = "권한 확인 및 추가 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(name = "festivalId", description = "리뷰를 생성할 festival의 id", in = ParameterIn.PATH) @PathVariable Long followingUserId){
        return followService.followingUser(userDetails,followingUserId);
    }

    @GetMapping("/users/follows/followers")
    @Operation(summary = "유저 팔로워 목록 조회 - 나를 팔로우 하는 유저", description = "나를 팔로우 하는 유저를 전체 조회합니다.")
    public ResponseEntity<List<SelectUserResponseDto>> selectFollowers(
            @Parameter(description = "권한 확인 및 조회 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "페이지 처리에 필요한 기본 설정")
        @PageableDefault(size=5, sort="createdAt", direction = Direction.DESC) Pageable pageable
        ){
        List<SelectUserResponseDto> results = followService.selectFollowers(userDetails, pageable);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/users/follows/followings")
    @Operation(summary = "유저 팔로잉 목록 조회 - 내가 팔로우 하는 유저", description = "내가 팔로우 하는 유저를 전체 조회합니다.")
    public ResponseEntity<List<SelectUserResponseDto>> selectFollowingUsers(
            @Parameter(description = "권한 확인 및 조회 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "페이지 처리에 필요한 기본 설정")
            @PageableDefault(size=5) Pageable pageable,
            @Parameter(description = "No offset 페이지 넘기기에 필요한 기본 설정")
            @RequestParam(value = "lt", required = false)Long lt
    ){
        if(userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);  // 401 Unauthorized 응답
        }
        List<SelectUserResponseDto> results = followService.selectFollowingUsers(userDetails, pageable, lt);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("users/follows/{followingUserId}")
    @Operation(summary = "유저 팔로우 취소", description = "@PathVariable을 통해 followingUserId를 받아와, 해당 User의 팔로우를 취소합니다")
    public ResponseEntity<ApiResponseDto> deleteFollowingUser(
            @Parameter(description = "권한 확인 및 취소 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingUserId){
        return followService.unfollowingUser(userDetails, followingUserId);
    }

    @PostMapping("/users/followed-festivals/{festivalId}")
    @Operation(summary = "페스티벌 팔로우", description = "@PathVariable을 통해 festivalId를 받아와, 해당 festival을 팔로우합니다")
    public ResponseEntity<ApiResponseDto> createFollowingFestival(
            @Parameter(description = "권한 확인 및 추가 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long festivalId){
        return followService.followingFestival(userDetails, festivalId);
    }

    @GetMapping("/users/followed-festivals")
    @Operation(summary = "페스티벌 팔로우 목록 조회", description = "내가 팔로우 하는 페스티벌을 전체 조회합니다.")
    public ResponseEntity<List<FestivalResponseDto>> selectFollowingFestivals(
            @Parameter(description = "권한 확인 및 조회 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "페이지 처리에 필요한 기본 설정")
        @PageableDefault(size=10, sort="createdAt", direction = Direction.DESC) Pageable pageable
        ){
        List<FestivalResponseDto> results = followService.selectFollowingFestivals(userDetails, pageable);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/users/followed-festivals/{festivalId}")
    @Operation(summary = "페스티벌 팔로우 취소", description = "@PathVariable을 통해 festivalId를 받아와, 해당 festival 팔로우를 취소합니다")
    public ResponseEntity<ApiResponseDto> deleteFollowingFestival(
            @Parameter(description = "권한 확인 및 취소 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long festivalId){
        return followService.unfollowingFestival(userDetails, festivalId);
    }
}
