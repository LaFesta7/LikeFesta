package com.sparta.lafesta.common.controller;

import com.sparta.lafesta.social.service.KakaoService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/users/sign-up")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/users/signup")
    public String signUp2() {
        return "sign-up";
    }

    @GetMapping("/users/festivals-map")
    public String festivalsMap() {
        return "kakaomap";
    }

    @GetMapping("/users/login-page")
    public String login(Model model) {
            String kakaoUrl = kakaoService.getKakaoLogin();
            model.addAttribute("kakaoUrl", kakaoUrl);
            return "login";
    }
}