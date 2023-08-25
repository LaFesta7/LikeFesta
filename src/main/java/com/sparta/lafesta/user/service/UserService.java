package com.sparta.lafesta.user.service;

import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.s3.entity.UserFileOnS3;
import com.sparta.lafesta.common.s3.repository.UserFileRepository;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    //CRUD
    private final UserRepository userRepository;
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
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
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

        // 사용자 등록
        User user = new User(username, password, email, role, nickname, organizerRequest);
        userRepository.save(user);


        //첨부파일 업로드
        if (files != null) {
            uploadFiles(files, user);
        }
    }

    //카카오 로그인 시 로그아웃
    public void kakaoLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

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

    private void deleteFiles(User user) {
        // 파일정보 불러오기
        List<UserFileOnS3> fileOnS3s = user.getUserFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (UserFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
            }
        }
    }

    private void modifyFiles(User user, List<MultipartFile> files) throws IOException {

        // 기존 파일 삭제
        deleteFiles(user);

        // 파일 등록
        uploadFiles(files, user);
    }


}