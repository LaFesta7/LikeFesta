package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.admin.service.AdminService;
import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.badge.repository.BadgeRepository;
import com.sparta.lafesta.badge.repository.UserBadgeRepository;
import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.repostiroy.ReviewRepository;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final FestivalRepository festivalRepository;
    private final ReviewRepository reviewRepository;
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

    // 뱃지 추가 조건 확인
    @Override
    @Transactional
    public void checkBadgeCondition(User user) {
        List<Badge> badges = badgeRepository.findAll();
        for (Badge badge : badges) {
            if(badge.getConditionEnum() == BadgeConditionEnum.FESTIVAL) {
                int festivalCount = 0;

                if (badge.getConditionFirstDay() == null && badge.getConditionLastDay() == null) {
                    festivalCount = reviewRepository.countByUser(user);
                } else {
                    // QueryDSL 등 개선 필요
                    List<Review> reviews = reviewRepository.findAllByUser(user);
                    LocalDateTime startDay = badge.getConditionFirstDay().atStartOfDay();
                    LocalDateTime endDay = badge.getConditionLastDay().atTime(LocalTime.MAX);
                    List<Festival> festivals = festivalRepository.findAllByOpenDateBetween(startDay, endDay);
                    for (Review review : reviews) {
                        Festival festival = review.getFestival();
                        if (festivals.contains(festival)) festivalCount++;
                    }
                }

                if (badge.getConditionStandard() <= festivalCount) {
                    createUserBadge(user, badge);
                }
            }
            // 리뷰 참여 - 페스티벌 참여 구분 합의 필요
            // 태그 완료 후 추가 구현 필요
        }
    }

    // 유저 뱃지 추가
    @Override
    @Transactional
    public void createUserBadge(User user, Badge badge) {
        if (userBadgeRepository.findByUserAndBadge(user, badge).isEmpty()) {
            UserBadge userBadge = new UserBadge(user, badge);
            userBadgeRepository.save(userBadge);
        }
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
