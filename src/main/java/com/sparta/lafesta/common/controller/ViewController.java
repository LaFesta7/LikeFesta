package com.sparta.lafesta.common.controller;

import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.follow.service.FollowService;
import com.sparta.lafesta.social.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ViewController {
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @Autowired
    private FollowService followService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/users/sign-up")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/festivals-map")
    public String festivalsMap() {
        return "kakaomap";
    }

    @GetMapping("/users/login-page")
    public String login(HttpServletRequest request, Model model) {

        String kakaoUrl = kakaoService.getKakaoLogin();
        model.addAttribute("kakaoUrl", kakaoUrl);
        return "login";
    }

    @GetMapping("/users/profile")
    public String mypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "mypage";
    }

    @GetMapping("/users/{userId}/profile-page")
    public String profile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "profile";
    }

    @GetMapping("/users/badge")
    public String badge(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "my-badge";
    }

    @GetMapping("/users/profile/followings-page")
    public String showFollowings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(userDetails.getUsername());
        return "my-follow";
    }

    @GetMapping("/users/profile/edit-page")
    public String editProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "mypage-edit";
    }

    @GetMapping("/users/withdrawal-page")
    public String withdrawUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "withdrawal";
    }

    @GetMapping("/festivals/{festivalId}/page")
    public String festivalPage() {
        return "festival";
    }

    @GetMapping("/festivals/post-page")
    public String postFestival(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-post";
    }

    @GetMapping("/festivals/{festivalId}/edit-page")
    public String editFestival(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-edit";
    }

    @GetMapping("/festival-requests/{festivalRequestId}/page")
    public String festivalRequestPage() {
        return "festival-request";
    }

    @GetMapping("/festival-requests/post-page")
    public String postFestivalRequest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-request-post";
    }

    @GetMapping("/festival-requests/{festivalRequestId}/edit-page")
    public String editFestivalRequest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-request-edit";
    }

    @GetMapping("/festivals/{fesitvalId}/reviews/{riviewId}/page")
    public String reviewPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "review";
    }

    @GetMapping("/festivals/{fesitvalId}/reviews/post-page")
    public String postReview(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "review-post";
    }

    @GetMapping("/festivals/{fesitvalId}/reviews/{riviewId}/edit-page")
    public String editReview(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "review-edit";
    }

    @GetMapping("/users/notification-page")
    public String notificationPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "notification";
    }


    @GetMapping("/admin-page")
    public String adminPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getUser().getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("해당 페이지에 접근하실 수 없습니다.");
        }
        return "admin";
    }
}