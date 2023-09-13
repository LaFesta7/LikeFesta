package com.sparta.lafesta.badge.service;

import com.sparta.lafesta.admin.service.AdminService;
import com.sparta.lafesta.badge.dto.BadgeRequestDto;
import com.sparta.lafesta.badge.dto.BadgeResponseDto;
import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeConditionEnum;
import com.sparta.lafesta.badge.entity.BadgeTag;
import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.badge.event.UserBadgeCreatedEventPublisher;
import com.sparta.lafesta.badge.repository.BadgeRepository;
import com.sparta.lafesta.badge.repository.BadgeRepositoryCustom;
import com.sparta.lafesta.badge.repository.BadgeTagRepository;
import com.sparta.lafesta.badge.repository.UserBadgeRepository;
import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.BadgeFileOnS3;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.s3.repository.BadgeFileRepository;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.repostiroy.ReviewRepository;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.tag.service.TagService;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;
    private final BadgeTagRepository badgeTagRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeRepositoryCustom badgeRepositoryCustom;
    private final FestivalRepository festivalRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final TagService tagService;

    //S3
    private final S3UploadService s3UploadService;
    private final BadgeFileRepository badgeFileRepository;
    private final String BADGE_FOLDER_NAME = "badge";

    // 알림
    private final UserBadgeCreatedEventPublisher eventPublisher;

    // 뱃지 생성
    @Override
    @Transactional
    public BadgeResponseDto createBadge(BadgeRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        // 뱃지 DB 저장
        Badge badge = new Badge(requestDto);
        badgeRepository.save(badge);

        // 뱃지 연관 태그 저장
        if (requestDto.getConditionEnum() == BadgeConditionEnum.TAG) {
            createBadgeTags(badge, requestDto);
        } else {
            if (requestDto.getConditionTags() != null) {
                throw new IllegalArgumentException("FREQUENCY와 STEADY는 태그를 선택할 수 없습니다.");
            }
        }

        // 첨부파일 업로드
        if (files != null) {
            uploadFiles(files, badge);
        }

        return new BadgeResponseDto(badge);
    }

    // 뱃지-태그 연관관계 저장
    @Override
    @Transactional
    public void createBadgeTags(Badge badge, BadgeRequestDto requestDto) {
        if (requestDto.getConditionTags() == null) {
            throw new IllegalArgumentException("태그를 추가해주세요.");
        }

        // 뱃지가 존재하면 해당 뱃지를 존재하지 않으면 새로운 태그를 생성해 연관관계 저장
        for (TagRequestDto tagRequestDto : requestDto.getConditionTags()) {
            Tag tag = tagService.checkTag(tagRequestDto);

            //중복 확인
            if (badgeTagRepository.findByBadgeAndTag(badge, tag).isPresent()) {
                throw new IllegalArgumentException("중복되는 태그입니다.");
            }
            //연관관계 생성
            badgeTagRepository.save(new BadgeTag(badge, tag));
        }
    }

    // 뱃지 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<BadgeResponseDto> selectBadges(User user, Pageable pageable) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        return badgeRepository.findAllBy(pageable).map(BadgeResponseDto::new);
    }

    // 뱃지 수정
    @Override
    @Transactional
    public BadgeResponseDto modifyBadge(Long badgeId, BadgeRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        Badge badge = findBadge(badgeId);

        //첨부파일 변경
        if (files != null) {
            modifyFiles(badge, files);
        }

        // 뱃지 정보 변경
        badge.modify(requestDto);

        // 뱃지 연관 태그 수정
        if (requestDto.getConditionEnum() == BadgeConditionEnum.TAG) {
            modifyBadgeTags(badge, requestDto);
        } else {
            if (requestDto.getConditionTags() != null) {
                throw new IllegalArgumentException("FREQUENCY와 STEADY는 태그를 선택할 수 없습니다.");
            }
        }

        return new BadgeResponseDto(badge);
    }

    // 뱃지-태그 연관관계 수정
    @Override
    @Transactional
    public void modifyBadgeTags(Badge badge, BadgeRequestDto requestDto) {
        if (requestDto.getConditionTags() == null) {
            throw new IllegalArgumentException("태그를 추가해주세요.");
        }

        // 연관관계가 존재하지 않을 경우에만 연관관계 생성 -> 태그가 없으면 새로 생성
        for (TagRequestDto tagRequestDto : requestDto.getConditionTags()) {
            Tag tag = tagService.checkTag(tagRequestDto);
            if (badgeTagRepository.findByBadgeAndTag(badge, tag).isEmpty()) {
                badgeTagRepository.save(new BadgeTag(badge, tag));
            }

        }

        //사용되지 않는 태그는 삭제
        List<String> beforeTags = badge.getBadgeTags().stream()
                .map(BadgeTag::getTag).map(Tag::getTitle).toList();
        List<String> requestTags = requestDto.getConditionTags().stream()
                .map(TagRequestDto::getTitle).toList();
        List<String> missingTags = beforeTags.stream()
                .filter(tag -> !requestTags.contains(tag)).toList();
        for (String tagTitles : missingTags) {
            Tag missingTag = tagService.findTagByTitle(tagTitles);
            badgeTagRepository.delete(findBadgeTag(badge,missingTag));
        }
    }

    // 뱃지 삭제
    @Override
    @Transactional
    public void deleteBadge(Long badgeId, User user) {
        // 관리자 권한 확인
        adminService.checkAdminRole(user);

        Badge badge = findBadge(badgeId);

        //첨부파일 DB에서 삭제
        deleteFiles(badge);

        // 뱃지 DB 삭제
        badgeRepository.delete(badge);
    }

    // 뱃지 조건 확인 후 추가
    @Override
    @Transactional
    public void checkBadgeCondition(User user) {
        List<Badge> badges = badgeRepository.findAll();
        for (Badge badge : badges) {
            BadgeConditionEnum conditionEnum = badge.getConditionEnum();
            LocalDateTime startDay = badge.getConditionFirstDay().atStartOfDay();
            LocalDateTime endDay = badge.getConditionLastDay().atTime(LocalTime.MAX);

            List<Review> reviews = reviewRepository.findAllByUser(user);

            // 전체 빈도수
            if (conditionEnum == BadgeConditionEnum.FREQUENCY) {
                checkBadgeFrequency(user, badge, reviews, startDay, endDay);
            }

            // 꾸준함
            if (conditionEnum == BadgeConditionEnum.STEADY) {
                checkBadgeSteady(user, badge, reviews, startDay, endDay);
            }

            // 장르별 빈도수
            if (conditionEnum == BadgeConditionEnum.TAG) {
                checkBadgeTagFrequency(user, badge, reviews, startDay, endDay);
            }
        }

    }

    // 전체 빈도 수에 따른 뱃지 추가
    @Transactional
    public void checkBadgeFrequency(User user, Badge badge, List<Review> reviews, LocalDateTime startDay, LocalDateTime endDay) {
        List<Festival> festivals = festivalRepository.findAllByOpenDateBetween(startDay, endDay);
        long festivalCount = reviews.stream().map(Review::getFestival).filter(festivals::contains).count();

        if (badge.getConditionStandard() <= festivalCount) {
            createUserBadge(user, badge);
        }
    }

    // 꾸준함에 따른 뱃지 추가
    @Transactional
    public void checkBadgeSteady(User user, Badge badge, List<Review> reviews, LocalDateTime startDay, LocalDateTime endDay) {
        Map<Integer, List<Festival>> festivalsByYear = reviews.stream()
                .map(Review::getFestival)
                .filter(festival -> festival.getOpenDate().isAfter(startDay) && festival.getOpenDate().isBefore(endDay))
                .collect(Collectors.groupingBy(festival -> festival.getOpenDate().getYear()));

        int consecutiveYears = 0;
        int targetYears = badge.getConditionStandard();

        int currentYear = Year.now().getValue();
        for (int year = currentYear; year >= currentYear - targetYears + 1; year--) {
            List<Festival> festivals = festivalsByYear.get(year);
            if (festivals != null && festivals.size() >= 1) {
                consecutiveYears++;
                if (consecutiveYears >= targetYears) {
                    createUserBadge(user, badge);
                    break;
                }
            } else {
                consecutiveYears = 0;
            }
        }
    }

    // 태그와 연관지어 장르별 빈도 수에 따른 뱃지 추가
    @Transactional
    public void checkBadgeTagFrequency(User user, Badge badge, List<Review> reviews, LocalDateTime startDay, LocalDateTime endDay) {
        List<Festival> festivals = festivalRepository.findAllByOpenDateBetween(startDay, endDay);
        List<Tag> badgeTags = badge.getBadgeTags().stream().map(BadgeTag::getTag).toList();

        int matchingFestivalCount = 0;
        for (Festival festival : festivals) {
            List<Tag> festivalTags = festival.getTags().stream().map(FestivalTag::getTag).toList();
            for (Review review : reviews) {
                if (review.getFestival().equals(festival)) {
                    if (festivalTags.stream().anyMatch(badgeTags::contains)) {
                        matchingFestivalCount++;
                    }
                }
            }
        }

        if (matchingFestivalCount >= badge.getConditionStandard()) {
            createUserBadge(user, badge);
        }
    }


    // 유저 뱃지 추가
    @Override
    @Transactional
    public void createUserBadge(User user, Badge badge) {
        if (userBadgeRepository.findByUserAndBadge(user, badge).isEmpty()) {
            UserBadge userBadge = new UserBadge(user, badge);
            userBadgeRepository.save(userBadge);
            // 이벤트 발생 -> 알림 생성
            eventPublisher.publishUserBadgeCreatedEvent(userBadge);
        }
    }

    // 나의 뱃지 보유 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponseDto> selectMyBadges(User user, Pageable pageable, Long lastBadge) {
        User selectUser = userService.findUser(user.getId());
        List<UserBadgeResponseDto> userBadges = badgeRepositoryCustom.findAllByUser(selectUser, pageable, lastBadge);
        return userBadges;
    }

    // 유저 뱃지 보유 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<UserBadgeResponseDto> selectUserBadges(Long userId, User user, Pageable pageable) {
        User selectUser = userService.findUser(userId);
        List<UserBadgeResponseDto> userBadges = userBadgeRepository.findAllByUser(selectUser, pageable)
                .stream().map(UserBadgeResponseDto::new).toList();
        return userBadges;
    }

    // 유저 대표 뱃지 설정/해제
    @Override
    @Transactional
    public UserBadgeResponseDto modifyUserBadgeRepresentative(Long badgeId, User user) {
        Badge badge = findBadge(badgeId);
        UserBadge userBadge = findUserAndBadge(user, badge);

        // 대표 뱃지는 3개가 넘지 않도록 설정
        if (!userBadge.getRepresentative() && userBadgeRepository.countByUserAndRepresentative(user, true) >= 3) {
            throw new IllegalArgumentException("대표 뱃지 설정은 3개까지 가능합니다.");
        }

        userBadge.modifyRepresentative();

        return new UserBadgeResponseDto(userBadge);
    }

    // S3 파일 업로드
    private void uploadFiles(List<MultipartFile> files, Badge badge) throws IOException {
        List<FileOnS3> fileOnS3s = new ArrayList<>();
        if (files != null) {
            fileOnS3s = s3UploadService.putObjects(files, BADGE_FOLDER_NAME, badge.getId());
        }

        // FielOnS3를 Festival로 변환
        for (FileOnS3 fileOnS3 : fileOnS3s) {
            //페스티벌 파일 S3 엔티티로 변환생성
            BadgeFileOnS3 badgeFileOnS3 = new BadgeFileOnS3(fileOnS3);
            //S3 엔티티에 페스티벌 연관관계 설정
            badgeFileOnS3.setBadge(badge);
            //DB저장
            badgeFileRepository.save(badgeFileOnS3);
        }
    }

    // s3 파일 수정
    private void modifyFiles(Badge badge, List<MultipartFile> files) throws IOException {

        // 기존 파일 삭제
        deleteFiles(badge);

        // 파일 등록
        uploadFiles(files, badge);
    }

    // s3 파일 삭제
    private void deleteFiles(Badge badge) {
        // 파일정보 불러오기
        List<BadgeFileOnS3> fileOnS3s = badge.getBadgeFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (BadgeFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
                badgeFileRepository.delete(fileOnS3);
            }
        }
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

    // 뱃지와 태그로 뱃지태그 찾기
    private BadgeTag findBadgeTag (Badge badge, Tag tag) {
        return badgeTagRepository.findByBadgeAndTag(badge, tag).orElseThrow(() ->
                new NotFoundException("선택한 뱃지태그는 존재하지 않습니다.")
        );
    }
}
