package com.sparta.lafesta.common.jwt;

import com.sparta.lafesta.common.refreshtoken.entity.UserToken;
import com.sparta.lafesta.common.refreshtoken.repository.RefreshTokenRepository;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    //1.JWT 데이터

    ////  access Token 관련 멤버  ////

    // Header KEY 값 --> 쿠키의 name 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; //-> Bearer 이 붙어 있으면 해당하는 값은 Token임을 알려주는 식별자.(규칙)
    // 토큰 만료시간
    private final int ACCESS_TOKEN_TIME_SECONDS = 30; //2시간으로 세팅. 테스트시 임의로 수정해야함
    private final long ACCESS_TOKEN_TIME = ACCESS_TOKEN_TIME_SECONDS * 1000L; //2시간으로 세팅. 테스트시 임의로 수정해야함

    //Token 블랙 리스트
    private final Set<String> tokenBlacklist = new HashSet<>();

    ////  Refresh Token 관련 멤버  ////
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private final int REFRESH_TOKEN_TIME_SECONDS = 14 * 24 * 60 * 60; // 14일 세팅
    private final long REFRESH_TOKEN_TIME = REFRESH_TOKEN_TIME_SECONDS * 1000L; // 14일 세팅


    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey

    private String secretKey; //application.properties에 선언되어 있는 값을 가져온다.
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //알고리즘 설정

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // 빈 주입
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @PostConstruct // 딱 한번만 받아오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지한다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    //// JWT를 생성 후 Cookie에 저장
    @Transactional
    public String[] addJwtToCookie(User user, HttpServletResponse res) {
        String[] tokens = new String[2];
        try {

            //토큰 생성
            String username = user.getUsername();
            UserRoleEnum role = user.getRole();

            String accessToken = createAccessToken(username, role);
            String refreshToken = createRefreshToken(username, role); //refresh token 생성

            tokens[0] = accessToken;
            tokens[1] = refreshToken;

            log.info("accessToken : " + accessToken);
            log.info("refreshToken : " + refreshToken);


            // 쿠키 생성
            Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER, tokenSpaceEncode(accessToken)); // Name-Value
            accessTokenCookie.setPath("/");
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_HEADER, tokenSpaceEncode(refreshToken)); // Name-Value
            refreshTokenCookie.setPath("/"); // 유효기간은 토큰만료시간이 있어서 따로 설정은 우선 안했습니다.

            // Response 객체에 Cookie 추가
            res.addCookie(accessTokenCookie);
            res.addCookie(refreshTokenCookie);

            // Redis서버에 토큰값 쌍 저장. 유저정보를 키로 지정.
            refreshTokenRepository.save(new UserToken(username, accessToken, refreshToken));

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return tokens;
    }

    //엑세스 토큰 생성
    public String createAccessToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    //리프레시 토큰 생성
    public String createRefreshToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }


    //쿠키에서 JWT가져오기 : HttpServletRequest 에서 Cookie Value
    public String[] getTokensFromRequest(HttpServletRequest req) throws UnsupportedEncodingException {
        Cookie[] cookies = req.getCookies();
        String[] tokens = new String[2]; // 0 : AccessToken, 1 : RefreshToken
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    tokens[0] = URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                }
                if (cookie.getName().equals(REFRESH_TOKEN_HEADER)) {
                    tokens[1] = URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        return tokens;
    }


    //// 토큰 검증
    public boolean validateToken(String token) {
        try {
            if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
                token = token.substring(7);
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
//            removeTokenFromCookie(response); //todo 만료된 토큰을 쿠키에서 제거하는 부분 체크하기.
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;

    }


    //// Cookie에 들어있던 JWT 토큰을 Substring
    // JWT 토큰 substring ->Bearer 을 잘라내기 위해서
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


    //// JWT에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    //블랙리스트에 추가
    public void addTokenToBlacklist(String token) {
        tokenBlacklist.add(token);
    }


    //공백 변환 " " -> "%20"
    private String tokenSpaceEncode(String token) throws UnsupportedEncodingException {
        return URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
    }

}