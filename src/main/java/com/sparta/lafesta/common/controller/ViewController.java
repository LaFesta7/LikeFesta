package com.sparta.lafesta.common.controller;

import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.follow.service.FollowService;
import com.sparta.lafesta.social.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

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

    @GetMapping("/users/festivals-map")
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
    public String festival1(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "mypage";
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
    public String festivalPage(@PathVariable Long festivalId) {
        return "festival";
    }

    @GetMapping("/festivals/post-page")
    public String festivalEdit(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-post";
    }

    @GetMapping("/festivals/{festivalId}/edit-page")
    public String editProfile(@PathVariable Long festivalId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-edit";
    }

    @GetMapping("/festivals/review-page")
    public String reviewPage() {
        return "review-show";
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