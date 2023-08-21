package com.sparta.lafesta.follow.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {
    private final FollowService followService;

    //유저 팔로우 추가
    @PostMapping("/users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ApiResponseDto> createFollowingUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingUserId){
        return followService.followingUser(userDetails,followingUserId);
    }

    //유저 팔로워 목록 조회 - 나를 팔로우 하는 유저
    @GetMapping("/users/{followedUserId}/followers")
    public void selectFollowers(){

    }

    //유저 팔로잉 목록 조회 - 내가 팔로우 하는 유저
    @GetMapping("/users/{followingUserId}/followings")
    public void selectFollowingUsers(){

    }

    //유저 팔로우 취소
    @DeleteMapping("users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ApiResponseDto> deleteFollowingUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingUserId){
        return followService.unfollowingUser(userDetails, followingUserId);
    }

    //페스티벌 팔로우
    @PostMapping("/users/{userId}/followed-festivals/{festivalId}")
    public ResponseEntity<ApiResponseDto> createFollowingFestival(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long festivalId){
        return followService.followingFestival(userDetails, festivalId);
    }

    //페스티벌 팔로우 목록 조회
    @GetMapping("/users/{userId}/followed-festivals")
    public void selectFollowingFestivals(){

    }

    //페스티벌 팔로우 취소
    @DeleteMapping("/users/{userId}/followed-festivals/{festivalId}")
    public ResponseEntity<ApiResponseDto> deleteFollowingFestival(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long festivalId){
        return followService.unfollowingFestival(userDetails, festivalId);
    }
}
