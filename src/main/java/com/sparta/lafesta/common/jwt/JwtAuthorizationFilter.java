package com.sparta.lafesta.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.refreshtoken.entity.UserToken;
import com.sparta.lafesta.common.refreshtoken.repository.RefreshTokenRepository;
import com.sparta.lafesta.common.security.UserDetailsServiceImpl;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String[] tokens = jwtUtil.getTokensFromRequest(req);

        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        if (StringUtils.hasText(accessToken)) {

            if (!jwtUtil.validateToken(accessToken)) {
                if (jwtUtil.validateToken(refreshToken)) {
                    //refreshToken은 살아있을 때 AccessToken과 RefreshToken 재발급 후 해당 Jwt로 인증인가 진행

                    log.info("accessToken 재발급");

                    Claims infoFromRefreshToken = jwtUtil.getUserInfoFromToken(refreshToken);

                    String username = infoFromRefreshToken.getSubject();
                    UserToken foundTokenDto = findFoundTokenDtoFromRedis(username);

                    if (foundTokenDto.getRefreshToken().substring(7).equals(refreshToken)) {
                        //재발급
                        User user = findUserByUsername(username);
                        jwtUtil.addJwtToCookie(user, res);
                    }
                } else {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.setContentType("application/json");
                    String result = new ObjectMapper().writeValueAsString(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN"));

                    res.getOutputStream().print(result);
                    return;
                }
            }

            Claims info;
            try {
                info = jwtUtil.getUserInfoFromToken(accessToken);
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.setContentType("application/json");
                String result = new ObjectMapper().writeValueAsString(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "INVALID_TOKEN"));

                res.getOutputStream().print(result);
                return;
            }

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserToken findFoundTokenDtoFromRedis(String username) {
        return refreshTokenRepository.findById(username).orElseThrow(()
                -> new NotFoundException("Redis서버에서 RefreshToken 정보를 찾을 수 없습니다."));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
