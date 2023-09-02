package com.sparta.lafesta.user.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.user.dto.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 로그아웃
    @GetMapping("/users/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃이 완료되었습니다.");
        response.sendRedirect("/");  // 특정 URL로 리디렉션
    }

    // 인플루언서 랭킹 조회
    @GetMapping("/users/rank")
    @Operation(summary = "인플루언서 랭킹 조회", description = "가장 팔로워 수가 많은 유저 TOP3를 조회합니다.")
    public ResponseEntity<List<SelectUserResponseDto>> selectUserRanking(
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<SelectUserResponseDto> results = userService.selectUserRanking(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    // 프로필 조회
    @GetMapping("/users/{userId}/profile")
    @Operation(summary = "프로필 조회", description = "Pathvariable로 유저 아이디를 받아 해당 유저의 프로필을 조회합니다.")
    public ResponseEntity<SelectUserResponseDto> selectUserProfile(
            @Parameter(description = "프로필을 조회할 user의 Id") @PathVariable Long userId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        SelectUserResponseDto result = userService.selectUserProfile(userId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    // 내 정보 조회
    @GetMapping("/users/info")
    @Operation(summary = "내 정보 조회", description = "로그인한 유저의 정보를 조회합니다.")
    public ResponseEntity<UserInfoResponseDto> selectUserInfo(
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserInfoResponseDto result = userService.selectUserInfo(userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    // 내 닉네임 수정
    @PutMapping("/users/info/nickname")
    @Operation(summary = "내 닉네임 수정", description = "Dto로 정보를 받아 로그인한 유저의 닉네임을 수정합니다.")
    public ResponseEntity<ApiResponseDto> modifyUserNickname(
            @Parameter(description = "수정할 닉네임 정보") @RequestBody NicknameRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserInfoResponseDto result = userService.modifyUserNickname(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "닉네임 수정이 완료되었습니다!"));
    }

    // 내 소개 수정
    @PutMapping("/users/info/introduce")
    @Operation(summary = "내 소개 수정", description = "Dto로 정보를 받아 로그인한 유저의 소개를 수정합니다.")
    public ResponseEntity<ApiResponseDto> modifyUserIntroduce(
            @Parameter(description = "수정할 소개 정보") @RequestBody IntroduceRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserInfoResponseDto result = userService.modifyUserIntroduce(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "소개 수정이 완료되었습니다!"));
    }

    // 내 이메일 수정
    @PutMapping("/users/info/email")
    @Operation(summary = "내 이메일 수정", description = "Dto로 정보를 받아 이메일 인증을 거친 후 로그인한 유저의 이메일을 수정합니다.")
    public ResponseEntity<ApiResponseDto> modifyUserEmail(
            @Parameter(description = "수정할 이메일 정보") @RequestBody MailRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserInfoResponseDto result = userService.modifyUserEmail(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "이메일 수정이 완료되었습니다!"));
    }

    // 내 비밀번호 수정
    @PutMapping("/users/info/password")
    @Operation(summary = "내 비밀번호 수정", description = "Dto로 정보를 받아 현재 비밀번호 확인 후 / 현재 비밀번호를 잊어버린 경우 이메일 인증을 받아 로그인한 유저의 비밀번호를 수정합니다.")
    public ResponseEntity<ApiResponseDto> modifyUserPassword(
            @Parameter(description = "수정할 비밀번호 정보") @RequestBody PasswordRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException("password는 8~15자이며 알파벳 대소문자와 숫자, 특수문자로 구성하여 다시 시도해주세요.");
        } else {
            UserInfoResponseDto result = userService.modifyUserPassword(requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "비밀번호 수정이 완료되었습니다!"));
        }
    }

    // 내 프로필 사진 수정
    @PutMapping("/users/info/image")
    @Operation(summary = "내 프로필 사진 수정", description = "이미지 정보를 받아 로그인한 유저의 프로필 이미지를 수정합니다.")
    public ResponseEntity<ApiResponseDto> modifyUserImage(
            @Parameter(description = "유저프로필 이미지 수정시 등록할 첨부 파일") @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        UserInfoResponseDto result = userService.modifyUserImage(files, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "프로필 사진 수정이 완료되었습니다!"));
    }

    // 유저 탈퇴
    @DeleteMapping("/users/withdrawal")
    @Operation(summary = "회원 탈퇴", description = "Dto로 정보를 받아 비밀번호 확인 후 로그인한 유저의 회원 탈퇴를 진행합니다.")
    public ResponseEntity<ApiResponseDto> deleteUser(
            @Parameter(description = "탈퇴 확인을 위한 패스워드 정보") @RequestBody WithdrawalRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        userService.deleteUser(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "회원 탈퇴가 완료되었습니다!"));
    }

    //인증 메일 발송
    @PostMapping("/users/mail-confirm")
    @Operation(summary = "이메일 인증 메일 발송", description = "String을 통해 이메일 주소을 받아옵니다. 받아온 이메일 주소로 인증 코드를 발송하고 발송한 인증 코드를 반환합니다.")
    ResponseEntity<ApiResponseDto> mailConfirm(
            @Parameter(description = "인증코드를 보낼 이메일 주소") @RequestBody MailRequestDto requestDto)
            throws Exception {
        userService.sendMailAndCreateVerificationCode(requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "인증 메일이 발송되었습니다."));
    }

    //인증 메일 발송-비밀번호 분실
    @PostMapping("/users/mail-confirm-password")
    @Operation(summary = "비밀번호를 분실한 경우 이메일 인증 메일 발송", description = "DB에 존재하는 user의 이메일 주소로 인증 코드를 발송하고 발송한 인증 코드를 반환합니다.")
    ResponseEntity<ApiResponseDto> mailConfirmPassword(
            @Parameter(description = "권한 확인을 위해 필요한 User 정보") @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws Exception {
        userService.sendMailAndCreateVerificationCodePassword(userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "인증 메일이 발송되었습니다."));
    }

    // 인증코드 확인
    @PutMapping("/users/verify-code")
    @Operation(summary = "이메일 인증 코드 확인", description = "클라이언트가 보낸 이메일 인증 코드를 확인합니다.")
    public ResponseEntity<ApiResponseDto> verifyCode(
            @Parameter(description = "이메일 인증 코드를 확인할 dto") @RequestBody VerificationRequestDto verificationRequestDto) {
        userService.verifyCode(verificationRequestDto);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "인증 코드가 일치합니다."));
    }
}