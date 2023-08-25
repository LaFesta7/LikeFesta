package com.sparta.lafesta.user.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "유저 관련 API", description = "유저 관련 API 입니다.")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/users/sign-up")
    @Operation(summary = "유저 회원가입", description = "ResponseDto를 통해 가입할 유저정보를 받아옵니다.")
    public ResponseEntity<ApiResponseDto> signup(
            @Parameter(description = "유저 정보를 받을 dto") @Valid @RequestBody SignupRequestDto requestDto,
            BindingResult bindingResult) {
        if (requestDto.getEmailAuth() != 1) {
            throw new IllegalArgumentException("이메일 인증 코드가 틀려 회원 등록이 불가능합니다.");
        }
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException(
                    "username은 4~10자이며 알파벳 소문자와 숫자로, password는 8~15자이며 알파벳 대소문자와 숫자, 특수문자로 구성하여 다시 시도해주세요.");
        } else {
            userService.signup(requestDto);
            return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다."));
        }
    }

    //인증 메일
    @PostMapping("users/sign-up/mail-confirm")
    @Operation(summary = "이메일 인증 메일 발송", description = "String을 통해 이메일 주소을 받아옵니다. 받아온 이메일 주소로 인증 코드를 발송하고 발송한 인증 코드를 반환합니다.")
    String mailConfirm(
            @Parameter(description = "인증코드를 보낼 이메일 주소") @RequestBody String email)
            throws Exception {
        String code = mailService.sendMessage(email);
        System.out.println("인증코드: " + code);
        return code;
    }

    //카카오로그인 로그아웃
    @GetMapping("/users/logout")
    public ResponseEntity<ApiResponseDto> logout(HttpServletRequest request,
                                                 HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "로그아웃이 완료되었습니다."));
    }
}