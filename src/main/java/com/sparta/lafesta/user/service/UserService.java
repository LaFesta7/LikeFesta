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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    // 회원가입
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

    //인플루언서 랭킹 조회
    @Transactional(readOnly = true)
    public List<SelectUserResponseDto> selectUserRanking(User user) {
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        return userRepository.findTop3User().stream()
                .map(SelectUserResponseDto::new).toList();
    }
    
    // 프로필 조회
    @Transactional(readOnly = true)
    public SelectUserResponseDto selectUserProfile(Long userId, User user) {
        return new SelectUserResponseDto(findUser(userId));
    }

    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponseDto selectUserInfo(User user) {
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

    // 내 소개 수정
    @Transactional
    public UserInfoResponseDto modifyUserIntroduce(IntroduceRequestDto requestDto, User user) {
        User selectUser = findUser(user.getId());
        selectUser.modifyIntroduce(requestDto.getIntroduce());
        return new UserInfoResponseDto(selectUser);
    }

    // 내 이메일 수정
    @Transactional
    public UserInfoResponseDto modifyUserEmail(MailRequestDto requestDto, User user) {
        // 이메일 중복 확인
        checkEmail(requestDto.getEmail());

        // 사용자 이메일 인증 확인
        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndConfirm(requestDto.getEmail(), true)
                .orElse(null);
        // 이메일 인증이 되지 않은 경우
        if (verificationCode == null) {
            log.error("이메일 미인증");
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        User selectUser = findUser(user.getId());
        selectUser.modifyEmail(requestDto.getEmail());
        return new UserInfoResponseDto(selectUser);
    }

    // 내 비밀번호 수정
    @Transactional
    public UserInfoResponseDto modifyUserPassword(PasswordRequestDto requestDto, User user) {
        User selectUser = findUser(user.getId());

        // 현재 비밀번호 확인
        if (!requestDto.getCurrentPassword().isBlank() &&
                !passwordEncoder.matches(requestDto.getCurrentPassword(), selectUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 틀렸습니다. 비밀번호를 잊어버리신 경우 이메일 인증을 진행해주세요.");
        }

        // 현재 비밀번호를 잊어버린 경우 이메일 인증 확인
        if (requestDto.getCurrentPassword().isBlank()) {
            // 사용자 이메일 인증 확인
            VerificationCode verificationCode = verificationCodeRepository.findByEmailAndConfirm(selectUser.getEmail(), true)
                    .orElse(null);
            // 이메일 인증이 되지 않은 경우
            if (verificationCode == null) {
                log.error("이메일 미인증");
                throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
            }
        }

        selectUser.modifyPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        return new UserInfoResponseDto(selectUser);
    }

    // 내 프로필 사진 수정
    @Transactional
    public UserInfoResponseDto modifyUserImage(List<MultipartFile> files, User user) throws IOException {
        User selectUser = findUser(user.getId());
        if (files == null) {
            deleteFiles(selectUser);
        } else {
            modifyFiles(selectUser, files);
        }
        return new UserInfoResponseDto(selectUser);
    }

    // 유저 탈퇴
    @Transactional
    public void deleteUser(WithdrawalRequestDto requestDto, User user) {
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        User selectUser = findUser(user.getId());
        userRepository.delete(selectUser);
    }

    // 인증 메일 발송 후 코드 DB 임시 저장하기
    @Transactional
    public void sendMailAndCreateVerificationCode(MailRequestDto requestDto) throws Exception {
        String email = requestDto.getEmail();

        checkEmail(email);

        String code = mailService.sendMessage(email);
        VerificationCode verificationCode = new VerificationCode(email, code);
        verificationCodeRepository.save(verificationCode);
    }

    // 비밀번호를 분실한 경우 인증 메일 발송 후 코드 DB 임시 저장하기
    @Transactional
    public void sendMailAndCreateVerificationCodePassword(User user) throws Exception {
        String email = findUser(user.getId()).getEmail();

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

    // 이메일 중복 확인
    public void checkEmail(String email) {
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("해당 이메일로 이미 가입하셨습니다.");
        }
    }

    // id로 유저 찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("선택한 유저는 존재하지 않습니다.")
        );
    }
}