package com.sparta.lafesta.user.service;

import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.s3.entity.UserFileOnS3;
import com.sparta.lafesta.common.s3.repository.UserFileRepository;
import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.user.dto.*;
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
import org.springframework.web.multipart.MultipartFile;
import com.sparta.lafesta.user.dto.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    //CRUD
    private final MailService mailService;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    //S3
    private final S3UploadService s3UploadService;
    private final UserFileRepository userFileRepository;
    private final String USER_FOLDER_NAME = "user";

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public void signup(SignupRequestDto requestDto, List<MultipartFile> files) throws IOException {
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

        checkEmail(email);

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
            if (requestDto.isOrganizer()) {
                throw new IllegalArgumentException("관리자나 주최자 한 ROLE로만 가입 가능합니다.");
            }
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

        // 사용자 등록 및 회원가입 완료된 인증코드 삭제
        User user = new User(username, password, email, role, nickname, organizerRequest);
        userRepository.save(user);
        verificationCodeRepository.delete(verificationCode);

        //첨부파일 업로드
        if (files != null) {
            uploadFiles(files, user);
        }
    }

    // 인증 메일 발송 후 코드 DB 임시 저장하기
    @Transactional
    public void sendMailAndCreateVerificationCode(MailConfirmRequestDto requestDto) throws Exception {
        String email = requestDto.getEmail();

        // 회원 중복 확인
        checkEmail(email);

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
            throw new IllegalArgumentException("인증코드가 만료되었거나 일치하지 않습니다.");
        }
        // 인증 코드가 만료된 경우
        if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.error("인증 코드 만료");
            throw new IllegalArgumentException("만료된 인증코드입니다.");
        }
        verificationCode.verificationCodeConfirm();
    }

    // 인증 코드 DB 30분마다 삭제하기
    @Transactional
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void cleanupExpiredVerificationCodes() {
        verificationCodeRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    //인플루언서 랭킹 조회
    @Transactional(readOnly = true)
    public List<SelectUserResponseDto> selectUserRanking(User user){
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        return userRepository.findTop3User().stream()
            .map(SelectUserResponseDto::new).toList();
    }

    //카카오 로그인 시 로그아웃
    public void kakaoLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 이메일 중복 확인
    public void checkEmail(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("해당 이메일로 이미 가입하셨습니다.");
        }
    }

    // s3 업로드
    private void uploadFiles(List<MultipartFile> files, User user) throws IOException {
        List<FileOnS3> fileOnS3s = new ArrayList<>();
        if (files != null) {
            fileOnS3s = s3UploadService.putObjects(files, USER_FOLDER_NAME, user.getId());
        }

        // FielOnS3를 User로 변환
        for (FileOnS3 fileOnS3 : fileOnS3s) {
            //페스티벌 파일 S3 엔티티로 변환생성
            UserFileOnS3 userFileOnS3 = new UserFileOnS3(fileOnS3);
            //S3 엔티티에 페스티벌 연관관계 설정
            userFileOnS3.setUser(user);
            //DB저장
            userFileRepository.save(userFileOnS3);
        }
    }

    // s3 삭제
    private void deleteFiles(User user) {
        // 파일정보 불러오기
        List<UserFileOnS3> fileOnS3s = user.getUserFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (UserFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
                userFileRepository.delete(fileOnS3);
            }
        }
    }

    // s3 수정
    private void modifyFiles(User user, List<MultipartFile> files) throws IOException {

        // 기존 파일 삭제
        deleteFiles(user);

        // 파일 등록
        uploadFiles(files, user);
    }

    // id로 유저 찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("선택한 유저는 존재하지 않습니다.")
        );
    }

    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponseDto findUserInfo(User user) {
        User selectUser = findUser(user.getId());
        return new UserInfoResponseDto(selectUser);
    }

    // 내 닉네임 수정
    @Transactional
    public UserInfoResponseDto modifyUserNickname(NicknameRequestDto requestDto, User user) {
        Optional<User> checkNickname = userRepository.findByNickname(requestDto.getNickname());
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }
        User selectUser = findUser(user.getId());
        selectUser.modifyNickname(requestDto.getNickname());
        return new UserInfoResponseDto(selectUser);
    }
}