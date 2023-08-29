package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.admin.service.AdminService;
import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.badge.repository.BadgeRepository;
import com.sparta.lafesta.badge.repository.UserBadgeRepository;
import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.exception.UnauthorizedException;
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
    public List<UserBadgeResponseDto> selectUserBadges(Long userId, User user) {
        User selectUser = userService.findUser(userId);
        List<UserBadgeResponseDto> userBadges = userBadgeRepository.findAllByUser(selectUser)
                .stream().map(UserBadgeResponseDto::new).toList();
        return userBadges;
    }

    // 유저 대표 뱃지 설정/해제
    @Override
    @Transactional
    public UserBadgeResponseDto modifyUserBadgeRepresentative(Long userId, Long badgeId, User user) {
        // 해당 유저가 맞는지 권한 확인
        if (user.getId() != userId) {
            throw new UnauthorizedException("해당 유저의 대표 뱃지를 설정/해제할 권한이 없습니다.");
        }

        Badge badge = findBadge(badgeId);
        UserBadge userBadge = findUserAndBadge(user, badge);

        // 대표 뱃지는 3개가 넘지 않도록 설정
        if (!userBadge.getRepresentative() && userBadgeRepository.countByUserAndRepresentative(user, true) >= 3) {
            throw new IllegalArgumentException("대표 뱃지 설정은 3개까지 가능합니다.");
        }

        userBadge.modifyRepresentative();

        return new UserBadgeResponseDto(userBadge);
    }

    // id로 뱃지 찾기
    private Badge findBadge(Long badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() ->
                new NotFoundException("선택한 뱃지는 존재하지 않습니다.")
        );
    }

    // 유저와 뱃지로 유저뱃지 찾기
    private UserBadge findUserAndBadge(User user, Badge badge) {
        return userBadgeRepository.findByUserAndBadge(user, badge).orElseThrow(() ->
                new NotFoundException("선택한 뱃지는 보유하고 있지 않습니다.")
        );
    }
}
