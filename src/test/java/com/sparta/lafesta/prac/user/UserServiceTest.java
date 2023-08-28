package com.sparta.lafesta.prac.user;

import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.repository.UserFileRepository;
import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.user.dto.SignupRequestDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.entity.VerificationCode;
import com.sparta.lafesta.user.repository.UserRepository;
import com.sparta.lafesta.user.repository.VerificationCodeRepository;
import com.sparta.lafesta.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
                        //issue BDDMockito와 비교했을 때 BDDMockito를 바로 배워도 괜찮을지?


@Slf4j
@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class UserServiceTest {
    //Mock객체를 선언해줍니다.
    @Mock
    UserRepository userRepository;
    @Mock
    S3UploadService s3UploadService;
    @Mock
    UserFileRepository userFileRepository;
    @Mock
    MailService mailService;
    @Mock
    VerificationCodeRepository verificationCodeRepository;
    @Mock
    PasswordEncoder passwordEncoder; //issue 이런것도 목객체로 넣어줘야하나?? 목객체를 아끼는 게 좋은 것 아닌가. @Autowired는 안된다.

    @Autowired //issue 이 부분을 테스트하기 위함인데, Autowired하지 않아도 테스트는 문제가없다. 어떻게 하는것이 정확한 테스트인가.
    UserService userService;
    SignupRequestDto requestDto;


    @Nested
    @DisplayName("회원가입 테스트 - 첨부파일 없이")
    class WithoutFiles {

        @BeforeEach
        void setUp() {
            userService = new UserService(mailService, userRepository, verificationCodeRepository,
                                            passwordEncoder, s3UploadService, userFileRepository);

            requestDto = new SignupRequestDto(
                    "testName",
                    "a1234567@",
                    "test@gmail.com",
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
            assertEquals("중복된 Username이 존재합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("관리자 회원가입 - 성공")
        public void checkAdminWithSuccessToken() throws IOException {
            ////Given

            //관리자 가입 시도
            requestDto.setAdmin(true);
            requestDto.setAdminToken(userService.getADMIN_TOKEN());
                // issue 이곳에 동적으로 토큰값 넣어주려고 UserService 코드를 수정했는데, 이렇게 영향을 줘도 괜찮은 지?

            VerificationCode verificationCode = new VerificationCode(requestDto.getEmail(), "test");
            verificationCode.verificationCodeConfirm();

            when(verificationCodeRepository.findByEmailAndConfirm(requestDto.getEmail(), true))
                    .thenReturn(Optional.of(verificationCode));


            ////WHEN

            userService.signup(requestDto, null); // 본코드는 최대한 유지하는 편이 좋다. 메서드를 추가하거나 오버로딩 사용.
//            User newUser = userService.signup(requestDto, null);
                //issue sighup메소드 안에서 생성된 객체를 확인하려면 return을 바꿔줘서 받아와야 하는지??
                    // issue 테스트만을 위한 코드수정이 괜찮은 지?


            ////THEN
//            assertEquals(newUser, new User(requestDto));
//            assertEquals(newUser.getRole(), UserRoleEnum.ADMIN);

        }

        @Test
        @DisplayName("관리자 회원가입 - 올바르지 않은 토큰")
        public void checkAdminWithFailToken() {
            ////Given

            //관리자 가입 시도
            requestDto.setAdmin(true);
            requestDto.setAdminToken("failToken");

            ////WHEN - THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto, null));
            assertEquals("관리자 암호가 틀려 등록이 불가능합니다.", exception.getMessage());

        }


        @Test
        @DisplayName("관리자 회원가입 - 주최자 역할 중복")
        public void checkAdminAndOrganizer() {
            ////Given

            //관리자 가입 시도
            requestDto.setAdmin(true);
            requestDto.setAdminToken("failToken");
            requestDto.setOrganizer(true);

            ////WHEN - THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto, null));
            assertEquals("관리자 암호가 틀려 등록이 불가능합니다.", exception.getMessage());

        }
    }
}
