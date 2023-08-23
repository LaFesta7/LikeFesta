package com.sparta.lafesta.social.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lafesta.common.jwt.JwtUtil;
import com.sparta.lafesta.social.dto.KakaoLoginDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.UUID;

@Slf4j(topic ="KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() { // 카카오 로그인 URL 생성
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public String kakaoLogin(String code) throws JsonProcessingException { // 카카오 로그인
        String accessToken = getToken(code); // 인가코드로 액세스 토큰 요청

        KakaoLoginDto kakaoLoginDto = getKakaoUserInfo(accessToken); // 액세스 토큰으로 카카오 유저 정보 요청

        User kakaoUser = registerKakaoUserIfNeeded(kakaoLoginDto);

        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());

        return createToken;
    }

    private String getToken(String code) throws JsonProcessingException { // 카카오 토큰 발급
        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_AUTH_URI)
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(); // 바디 설정
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URL);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity // 요청 보내기
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange( // 응답 받기
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody()); // 토큰 값 꺼내기
        return jsonNode.get("access_token").asText();
    }

    // 카카오 유저 정보 가져오기
    private KakaoLoginDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_API_URI)
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 만들기
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // JSON -> 자바객체로 변환
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody()); // 유저 정보 꺼내기
        Long id = jsonNode.get("id").asLong(); // 카카오에서 제공하는 고유 id
        String nickname = jsonNode.get("properties") // 카카오에서 제공하는 닉네임
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account") // 카카오에서 제공하는 이메일
                .get("email").asText();

        // 로그인한 유저 정보 로그로 찍기
        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoLoginDto(id, nickname, email); // 카카오 사용자 정보 반환
    }

    // 카카오 유저 정보로 회원가입
    private User registerKakaoUserIfNeeded(KakaoLoginDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {

            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;

                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = kakaoUserInfo.getEmail();
                String nickname = kakaoUserInfo.getNickname();

                kakaoUser = new User(kakaoUserInfo.getNickname(), encodedPassword, email, UserRoleEnum.USER, nickname, kakaoId);
            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
