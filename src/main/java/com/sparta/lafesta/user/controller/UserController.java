package com.sparta.lafesta.user.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.user.dto.MailConfirmRequestDto;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.dto.VerificationRequestDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "유저 관련 API", description = "유저 관련 API 입니다.")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/users/sign-up")
    @Operation(summary = "유저 회원가입", description = "ResponseDto를 통해 가입할 유저정보를 받아옵니다.")
    public ResponseEntity<ApiResponseDto> signup(
            @Parameter(description = "유저 정보를 받을 dto") @Valid @RequestBody SignupRequestDto requestDto,
			@Parameter(description = "유저프로필 생성시 등록할 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
			BindingResult bindingResult
	) throws IOException {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException("username은 4~10자이며 알파벳 소문자와 숫자로, password는 8~15자이며 알파벳 대소문자와 숫자, 특수문자로 구성하여 다시 시도해주세요.");
        } else {
            userService.signup(requestDto, files);
            return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다."));
        }
    }

    //인증 메일 발송
    @PostMapping("/users/sign-up/mail-confirm")
    @Operation(summary = "이메일 인증 메일 발송", description = "String을 통해 이메일 주소을 받아옵니다. 받아온 이메일 주소로 인증 코드를 발송하고 발송한 인증 코드를 반환합니다.")
    ResponseEntity<ApiResponseDto> mailConfirm(
            @Parameter(description = "인증코드를 보낼 이메일 주소") @RequestBody MailConfirmRequestDto requestDto)
            throws Exception {
        userService.sendMailAndCreateVerificationCode(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "인증 메일이 발송되었습니다."));
    }

    // 인증코드 확인
    @PutMapping("/users/sign-up/verify-code")
    @Operation(summary = "이메일 인증 코드 확인", description = "클라이언트가 보낸 이메일 인증 코드를 확인합니다.")
    public ResponseEntity<ApiResponseDto> verifyCode(
            @Parameter(description = "이메일 인증 코드를 확인할 dto") @RequestBody VerificationRequestDto verificationRequestDto) {
        userService.verifyCode(verificationRequestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "인증 코드가 일치합니다."));
    }

    // 로그아웃
    @GetMapping("/users/logout")
    public ResponseEntity<ApiResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "로그아웃이 완료되었습니다."));
    }
}