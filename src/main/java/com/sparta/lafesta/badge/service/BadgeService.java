package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface BadgeService {
    /**
     * 뱃지 생성
     * @param requestDto 뱃지 생성 정보
     * @param user       권한 확인
     * @return 뱃지 생성 결과
     */
    BadgeResponseDto createBadge(BadgeRequestDto requestDto, User user);

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
     * @param user       권한 확인
     * @return 뱃지 수정 결과
     */
    BadgeResponseDto modifyBadge(Long badgeId, BadgeRequestDto requestDto, User user);

    /**
     * 뱃지 삭제
     * @param badgeId 수정할 뱃지 id
     * @param user       권한 확인
     * @return 뱃지 삭제 결과
     */
    void deleteBadge(Long badgeId, User user);

    /**
     * 유저 뱃지 추가
     * @param userId 추가할 유저 id
     * @param badgeId 수정할 뱃지 id
     * @param user       권한 확인
     * @return 뱃지 추가 결과
     */
    void createUserBadge(Long userId, Long badgeId, User user);

    /**
     * 유저 뱃지 보유 목록 조회
     * @param userId 조회활 유저 id
     * @param user       권한 확인
     * @return 뱃지 보유 목록 조회 결과
     */
    List<BadgeResponseDto> selectUserBadges(Long userId, User user);
}
