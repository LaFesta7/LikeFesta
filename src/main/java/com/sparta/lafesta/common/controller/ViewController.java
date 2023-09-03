package com.sparta.lafesta.common.controller;

import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.social.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
            String jwtToken = jwtUtil.getJwtFromHeader(request);

            if (jwtToken != null && jwtUtil.validateToken(jwtToken)) {
                return "redirect:/";
            }

            String kakaoUrl = kakaoService.getKakaoLogin();
            model.addAttribute("kakaoUrl", kakaoUrl);
            return "login";
    }

    @GetMapping("/users/fest")
    public String festival1(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-post";
    }

    @GetMapping("/users/fest-edit")
    public String festivalEdit(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "festival-edit";
    }

    @GetMapping("/users/badge")
    public String badge(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "my-badge";
    }

}