package com.sparta.lafesta.prac.user;

import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.repository.UserFileRepository;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.repository.UserRepository;
import com.sparta.lafesta.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class UserServiceTest {
    //Mock객체를 선언해줍니다.
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    S3UploadService s3UploadService;
    @Mock
    UserFileRepository userFileRepository;

    UserService userService;
    SignupRequestDto requestDto;


    @Nested
    @DisplayName("첨부파일 없이")
    class WithoutFiles {

        @BeforeEach
        void setUp() {
            userService = new UserService(userRepository, passwordEncoder, s3UploadService, userFileRepository);

            requestDto = new SignupRequestDto(
                    "testName",
                    "a1234567@",
                    "test@gmail.com",
                    0,
                    "nicknameTest",
                    false,
                    "",
                    false
            );

            List<MultipartFile> files = null;
        }


        @Test
        @DisplayName("중복유저 체크")
        public void existedUser() throws IOException {

            ////Given

            //이미 가입된 유저 객체 생성
            User user = new User(requestDto);
            log.info(requestDto.getUsername());

            //회원가입 정보로 만든 동일한 User객체를 반환해줘, 기존 유저랑 중복되는 상황 구현.
            given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(user));


            ////WHEN - THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto, null));
            assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
        }

        @Test
        public void checkAdmin() {
            ////Given


            ////WHEN




            ////THEN



        }
    }
}
