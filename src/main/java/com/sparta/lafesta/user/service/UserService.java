package com.sparta.lafesta.user.service;

import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.user.dto.MailConfirmRequestDto;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.dto.VerificationRequestDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.entity.VerificationCode;
import com.sparta.lafesta.user.repository.UserRepository;
import com.sparta.lafesta.user.repository.VerificationCodeRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        Boolean organizerRequest = false;

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 Username이 존재합니다.");
        }

        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        if (requestDto.isOrganizer()) {
            organizerRequest = true;
        }

        // 사용자 이메일 인증 확인
        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndConfirm(email, true)
                .orElse(null);
        // 이메일 인증이 되지 않은 경우
        if (verificationCode == null) {
            log.error("이메일 미인증");
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // 사용자 등록
        User user = new User(username, password, email, role, nickname, organizerRequest);
        userRepository.save(user);
    }

    // 인증 메일 발송 후 코드 DB 임시 저장하기
    @Transactional
    public void sendMailAndCreateVerificationCode(MailConfirmRequestDto requestDto) throws Exception {
        String email = requestDto.getEmail();

        // 회원 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("해당 이메일로 이미 가입하셨습니다.");
        }

        String code = mailService.sendMessage(email);
        VerificationCode verificationCode = new VerificationCode(email, code);
        verificationCodeRepository.save(verificationCode);
    }

    // 인증코드 확인하기
    @Transactional
    public void verifyCode(VerificationRequestDto verificationRequestDto) {
        String email = verificationRequestDto.getEmail();
        String code = verificationRequestDto.getVerificationCode();

        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndCode(email, code)
                .orElse(null);

        // 인증 코드가 일치하지 않는 경우
        if (verificationCode == null) {
            log.error("인증 코드 불일치");
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        // 인증 코드가 만료된 경우
        if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.error("인증 코드 만료");
            throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
        }
        verificationCode.verificationCodeConfirm();
    }

    // 인증 코드 DB 30분마다 삭제하기
    @Transactional
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void cleanupExpiredVerificationCodes() {
        verificationCodeRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    //카카오 로그인 시 로그아웃
    public void kakaoLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}