package com.sparta.lafesta.user.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.dto.UserPasswordRequestDto;
import com.sparta.lafesta.user.dto.UserProfileRequestDto;
import com.sparta.lafesta.user.dto.UserProfileResponseDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
    }

    //카카오 로그인 시 로그아웃
    public void kakaoLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public ApiResponseDto getUserProfile(User user, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            return new UserProfileResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getIntroduce());
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
    }
    // 프로필 변경 메서드
    @Transactional
    public ApiResponseDto modifyUserProfile(User user, UserProfileRequestDto requestDto, HttpServletResponse response){
        User targetUser = findUser(user.getUsername());
        if(targetUser!=null){
            targetUser.modifyProfile(requestDto);
            response.setStatus(201);
            return new ApiReponseDto("프로필 변경 완료.",response.getStatus());
        }else {
            throw new IllegalArgumentException("해당 아이디는 존재하지 않습니다.");
        }
    }



    // 회원탈퇴 메서드
    public boolean withdrawl(String username, String password) {
        //  }
//}
        userRepository.delete();
        return false;
    }

    // 비밀번호를 변경하는 메서드
    // 1. 유저가 존재하면 그 유저의 password와 bodu에 입력된 password가 일치하는지 확인.
    // 2. 일치할 경우, 수정된 비밀번호값으로 변경.
    @Transactional
    public ApiResponseDto modifyUserPassword(User user, UserPasswordRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(user.getUsername());
        if (targetUser != null) {
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                response.setStatus(201);
                targetUser.modifyPassword(passwordEncoder.encode(requestDto.getModifyPassword()));
            } else {
                throw new IllegalArgumentException("비밀번호가 틀립니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하는 사용자가 아닙니다.");
        }
        return new ApiResponseDto(response.getStatus(), "비밀번호 변경이 완료되었습니다.");

    }
}