package com.sparta.lafesta.common.jwt;

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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    // Header KEY 값 --> 쿠키의 name 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; //-> Bearer 이 붙어 있으면 해당하는 값은 Token임을 알려주는 식별자.(규칙)

    //Token 블랙 리스트
    private final Set<String> tokenBlacklist = new HashSet<>();


    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    //    private final long TOKEN_TIME = 60 * 1000L;
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey

    private String secretKey; //application.properties에 선언되어 있는 값을 가져온다.
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //알고리즘 설정

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct // 딱 한번만 받아오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지한다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //2.JWT 생성
    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    //3.생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    //4.Cookie에 들어있던 JWT 토큰을 Substring
    // JWT 토큰 substring ->Bearer 을 잘라내기 위해서
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        } else {
            return tokenValue;
        }
//        logger.error("Not Found Token");
//        throw new NullPointerException("Not Found Token");
    }

    //5.JWT 검증
    // 토큰 검증
    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {

            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            removeTokenFromCookie(response);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;

    }

    //6.JWT에서 사용자 정보 가져오기
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    public String resolveToken(HttpServletRequest request) {
        String bearerToken= request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }
    //쿠키에 있는 토큰 제거
    public void removeTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = createExpiredCookie();
        response.addCookie(cookie);
    }

    // 만료된 쿠키 생성
    private Cookie createExpiredCookie() {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
    //블랙리스트에 추가
    public void addTokenToBlacklist(String token) {
        tokenBlacklist.add(token);
    }

}