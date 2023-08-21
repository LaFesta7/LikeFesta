package com.sparta.lafesta.social.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.social.service.KakaoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoLoginController {
    private final KakaoService kakaoService;

    @GetMapping("/user/kakao/login") // 카카오 로그인 페이지
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin()); // 카카오 로그인 URL
        return "login";
    }

    @GetMapping("/user/kakao/callback") // 카카오 로그인 완료 후 리다이렉트 주소
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code); // 카카오 로그인 후 토큰 발급

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7)); // 토큰 앞에 "Bearer " 제거
        cookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        response.addCookie(cookie); // 응답에 쿠키 추가

        return "redirect:/"; // 메인 페이지로 리다이렉트
    }
}