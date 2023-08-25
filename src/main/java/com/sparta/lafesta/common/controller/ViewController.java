package com.sparta.lafesta.common.controller;

import com.sparta.lafesta.social.service.KakaoService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final KakaoService kakaoService;

    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/api/users/sign-up")
    public String signUp() {return "sign-up";}

    @GetMapping("/api/users/login-page")
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
        return "login";
    }
}