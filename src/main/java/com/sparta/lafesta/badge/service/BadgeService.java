package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BadgeService {
    /**
     * 뱃지 생성
     * @param requestDto 뱃지 생성 정보
     * @param files      뱃지 이미지 정보
     * @param user       권한 확인
     * @return 뱃지 생성 결과
     */
    BadgeResponseDto createBadge(BadgeRequestDto requestDto, List<MultipartFile> files, User user) throws IOException;

    /**
     * 뱃지-태그 연관관계 저장
     * @param badge 뱃지 정보
     * @param requestDto 뱃지 생성 정보
     */
    void createBadgeTags(Badge badge, BadgeRequestDto requestDto);

    /**
     * 뱃지 전체 조회
     * @param user       권한 확인
     * @return 뱃지 전체 조회 결과
     */
    List<BadgeResponseDto> selectBadges(User user);

    /**
     * 뱃지 수정
     * @param badgeId 수정할 뱃지 id
     * @param requestDto 뱃지 수정 정보
     * @param files      뱃지 수정 이미지 정보
     * @param user       권한 확인
     * @return 뱃지 수정 결과
     */
    BadgeResponseDto modifyBadge(Long badgeId, BadgeRequestDto requestDto, List<MultipartFile> files, User user) throws IOException;

    /**
     * 뱃지 태그 연관관계 수정
     * @param badge 수정할 뱃지
     * @param requestDto 뱃지 수정 정보
     */
    void modifyBadgeTags(Badge badge, BadgeRequestDto requestDto);

    /**
     * 뱃지 삭제
     * @param badgeId 수정할 뱃지 id
     * @param user       권한 확인
     * @return 뱃지 삭제 결과
     */
    void deleteBadge(Long badgeId, User user);

    /**
     * 유저 뱃지 조건 확인
     * @param user 조건을 확인할 유저
     */
    void checkBadgeCondition(User user);

    /**
     * 유저 뱃지 추가
     * @param user 추가할 유저
     * @param badge 추가할 뱃지
     */
    void createUserBadge(User user, Badge badge);

    /**
     * 유저 뱃지 보유 목록 조회
     * @param userId 조회활 유저 id
     * @param user       권한 확인
     * @return 뱃지 보유 목록 조회 결과
     */
    List<UserBadgeResponseDto> selectUserBadges(Long userId, User user);

    /**
     * 유저 대표 뱃지 설정/해제
     * @param userId 설정할 유저 id
     * @param badgeId 설정할 뱃지 id
     * @param user       권한 확인
     * @return 대표 뱃지 목록
     */
    UserBadgeResponseDto modifyUserBadgeRepresentative(Long userId, Long badgeId, User user);
}
