package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.admin.service.AdminService;
import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.badge.repository.BadgeRepository;
import com.sparta.lafesta.badge.repository.UserBadgeRepository;
import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserService userService;
    private final AdminService adminService;

    // 뱃지 생성
    @Override
    @Transactional
    public BadgeResponseDto createBadge(BadgeRequestDto requestDto, User user) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        Badge badge = new Badge(requestDto);
        badgeRepository.save(badge);
        return new BadgeResponseDto(badge);
    }

    // 뱃지 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<BadgeResponseDto> selectBadges(User user) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        return badgeRepository.findAll().stream().map(BadgeResponseDto::new).toList();
    }

    // 뱃지 수정
    @Override
    @Transactional
    public BadgeResponseDto modifyBadge(Long badgeId, BadgeRequestDto requestDto, User user) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        Badge badge = findBadge(badgeId);
        badge.modify(requestDto);
        return new BadgeResponseDto(badge);
    }

    // 뱃지 삭제
    @Override
    @Transactional
    public void deleteBadge(Long badgeId, User user) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        Badge badge = findBadge(badgeId);
        badgeRepository.delete(badge);
    }

    // 유저에게 뱃지 추가
    @Override
    @Transactional
    public void createUserBadge(Long userId, Long badgeId, User user) {
        User addUser = userService.findUser(userId);
        Badge badge = findBadge(badgeId);
        UserBadge userBadge = new UserBadge(addUser, badge);
    }

    // 유저 뱃지 보유 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<BadgeResponseDto> selectUserBadges(Long userId, User user) {
        User selectUser = userService.findUser(userId);
        List<UserBadge> userBadges = userBadgeRepository.findAllByUser(selectUser);
        List<BadgeResponseDto> badgeResponseDtos = new ArrayList<>();
        for (UserBadge userBadge : userBadges) {
            Badge badge = findBadgeByUserBadge(userBadge);
            badgeResponseDtos.add(new BadgeResponseDto(badge));
        }
        return badgeResponseDtos;
    }

    // id로 뱃지 찾기
    private Badge findBadge(Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() ->
                new NotFoundException("선택한 뱃지는 존재하지 않습니다.")
        );
    }

    // 유저뱃지로 뱃지 찾기
    private Badge findBadgeByUserBadge(UserBadge userBadge) {
        return badgeRepository.findByUserBadges(userBadge).orElseThrow(() ->
                new NotFoundException("선택한 뱃지는 존재하지 않습니다.")
        );
    }
}
