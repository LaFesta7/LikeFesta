package com.sparta.lafesta.festival.service;


import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.s3.repository.FestivalFileRepository;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.event.FestivalCreatedEventPublisher;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.festival.repository.FestivalRepositoryCustom;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.like.festivalLike.repository.FestivalLikeRepository;
import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.notification.entity.FestivalReminderType;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.tag.service.TagServiceImpl;
import com.sparta.lafesta.user.entity.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {

    //CRUD
    private final FestivalRepository festivalRepository;
    private final FestivalRepositoryCustom festivalRepositoryCustom;

    //S3
    private final S3UploadService s3UploadService;
    private final FestivalFileRepository festivalFileRepository;
    private final String FESTIVAL_FOLDER_NAME = "festival";

    //Like
    private final FestivalLikeRepository festivalLikeRepository;

    // 알림
    private final FestivalCreatedEventPublisher eventPublisher;

    @Autowired
    private TransactionTemplate transactionTemplate;

    //Tag
    private final TagServiceImpl tagService;


    // 페스티벌 등록
    @Override
    @Transactional
    public FestivalResponseDto createFestival(FestivalRequestDto requestDto,
                                              List<MultipartFile> files, User user) throws IOException {
//  public FestivalResponseDto createFestival(FestivalRequestDto requestDto, User user) throws IOException {

        // 허가되지 않은 주최사, 일반 사용자 접근 시 예외처리
        if (user.getRole().getAuthority().equals("ROLE_USER")) {
            throw new UnauthorizedException("해당 요청에 접근할 수 없습니다.");
        }
        Festival festival = new Festival(requestDto, user);

        //festival DB 저장
        Festival savedFestival = festivalRepository.save(festival);

        // 첨부파일업로드 -> 이후 업로드된 파일의 url주소를 festival객체에 담아줄 예정.
        if (files != null) {
            uploadFiles(files, festival);
        }

        //태그 생성 및 추가
        createFestivalTag(savedFestival, requestDto.getTagList());

        // 이벤트 발생 -> 알림 생성
        eventPublisher.publishFestivalCreatedEvent(festival);

        return new FestivalResponseDto(festival);
    }


    // 페스티벌 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFestivals(Pageable pageable) {
        return festivalRepository.findAllBy(pageable).stream()
                .map(FestivalResponseDto::new).toList();
    }


    // 페스티벌 상세 조회
    @Override
    @Transactional(readOnly = true)
    public FestivalResponseDto selectFestival(Long festivalId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        return new FestivalResponseDto(findFestival(festivalId));
    }

    // 페스티벌 내용 수정
    @Override
    @Transactional
    public FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto,
                                              List<MultipartFile> files, User user) throws IOException {
        Festival festival = findFestival(festivalId);

        // 주최사는 본인이 작성한 글만 수정 가능
        if (user.getRole().getAuthority().equals("ROLE_ORGANIZER")
                && !festival.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        // 관리자는 관리자가 작성한 글만 수정 가능
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")
                && festival.getUser().getRole().getAuthority().equals("ROLE_ORGANIZER")) {
            throw new UnauthorizedException("주최사가 작성한 글은 관리자 권한으로 수정할 수 없습니다.");
        }

        //첨부파일 변경
        if (files != null) {
            modifyFiles(festival, files);
        }
        //페스티벌 정보 변경
        festival.modify(requestDto);

        //이전 태그 정보 삭제
        deleteFestivalTag(festival);
        //새로운 태그 생성 및 추가
        createFestivalTag(festival, requestDto.getTagList());

        return new FestivalResponseDto(festival);
    }


    // 페스티벌 삭제
    @Override
    @Transactional
    public void deleteFestival(Long festivalId, User user) {
        Festival festival = findFestival(festivalId);

        // 주최사의 경우 본인이 작성한 글만, 관리자는 모든 글에 대하여 삭제 가능
        if (!festival.getUser().getId().equals(user.getId())
                && !user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("해당 요청에 접근할 수 없습니다.");
        }

        //첨부파일  DB에서 삭제
        deleteFiles(festival);

        List<FestivalTag> festivalTags = tagService.findFestivalTagsByFestival(festival);

        festivalRepository.delete(festival);

        //연관된 태그 정리
        for (FestivalTag festivalTag : festivalTags) {
            tagService.deleteUnusedTag(festivalTag.getTag());
        }
    }


    // 페스티벌 좋아요 추가
    @Override
    @Transactional
    public FestivalResponseDto createFestivalLike(Long festivalId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        Festival festival = findFestival(festivalId);
        // 좋아요를 이미 누른 경우 오류 반환
        if (findFestivalLike(user, festival) != null) {
            throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 페스티벌에 좋아요 추가
        festivalLikeRepository.save(new FestivalLike(user, festival));

        return new FestivalResponseDto(festival);
    }

    // 페스티벌 좋아요 취소
    @Override
    @Transactional
    public FestivalResponseDto deleteFestivalLike(Long festivalId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        FestivalResponseDto response = transactionTemplate.execute(status -> {
            Festival festival = findFestival(festivalId);
            // 좋아요를 누르지 않은 경우 오류 반환
            if (findFestivalLike(user, festival) == null) {
                throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
            }
            // 오류가 나지 않을 경우 해당 페스티벌에 좋아요 취소
            festivalLikeRepository.delete(findFestivalLike(user, festival));

            // 여기에서 커밋을 수행 (트랜잭션 내에서 커밋 또는 롤백을 수행할 수 있음)
            status.flush();

            // FestivalResponseDto 생성 후 반환
            return new FestivalResponseDto(festival);
        });

        // 위에서 커밋이 수행되었으므로 FestivalResponseDto에서 새로운 likeCnt를 가져올 수 있음
        return response;
    }

    //페스티벌 랭킹 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFestivalRanking(User user){
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        return festivalRepositoryCustom.findTop3Festival().stream()
            .map(FestivalResponseDto::new).toList();
    }

    // 페스티벌 오픈 알림을 보낼 페스티벌 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<ReminderDto> getFestivalOpenReminders() {
        // 페스티벌 개최 당일, 1일 전, 7일 전 발송
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate sevenDaysAfter = today.plusDays(7);

        List<LocalDate> dateRanges = Arrays.asList(today, tomorrow, sevenDaysAfter);

        return getReminders(dateRanges, FestivalReminderType.FESTIVAL_OPEN);
    }

    // 페스티벌 예매 오픈 알림을 보낼 페스티벌 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<ReminderDto> getReservationOpenReminders() {
        // 페스티벌 예매 오픈 당일, 1일 전 발송
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<LocalDate> dateRanges = Arrays.asList(today, tomorrow);

        return getReminders(dateRanges, FestivalReminderType.RESERVATION_OPEN);
    }

    // 페스티벌 리뷰 독려 알림을 보낼 페스티벌 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<ReminderDto> getReviewEncouragementReminders() {
        // 페스티벌 종료 1일 후 발송
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<LocalDate> dateRanges = List.of(yesterday);

        return getReminders(dateRanges, FestivalReminderType.REVIEW_ENCOURAGEMENT);
    }

    // 알림을 보낼 페스티벌 가져오기
    private List<ReminderDto> getReminders(List<LocalDate> dateRanges, FestivalReminderType type) {
        List<Festival> reminders = dateRanges.stream()
                .map(date -> {
                    LocalDateTime startOfDay = date.atStartOfDay();
                    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                    Stream<Festival> festivalStream;
                    if (type == FestivalReminderType.FESTIVAL_OPEN) {
                        festivalStream = festivalRepository.findAllByOpenDateBetween(startOfDay, endOfDay).stream();
                    } else if (type == FestivalReminderType.RESERVATION_OPEN) {
                        festivalStream = festivalRepository.findAllByReservationOpenDateBetween(startOfDay, endOfDay).stream();
                    } else if (type == FestivalReminderType.REVIEW_ENCOURAGEMENT) {
                        festivalStream = festivalRepository.findAllByEndDateBetween(startOfDay, endOfDay).stream();
                    } else {
                        festivalStream = Stream.empty();
                    }
                    return festivalStream.toList();
                })
                .flatMap(List::stream)
                .toList();

        return reminders.stream()
                .map(festival -> new ReminderDto(festival, type)).toList();
    }

    // id로 페스티벌 가져오기
    public Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌은 존재하지 않습니다.")
        );
    }

    // s3 업로드
    private void uploadFiles(List<MultipartFile> files, Festival festival) throws IOException {
        List<FileOnS3> fileOnS3s = new ArrayList<>();
        if (files != null) {
            fileOnS3s = s3UploadService.putObjects(files, FESTIVAL_FOLDER_NAME, festival.getId());
        }

        // FielOnS3를 Festival로 변환
        for (FileOnS3 fileOnS3 : fileOnS3s) {
            //페스티벌 파일 S3 엔티티로 변환생성
            FestivalFileOnS3 festivalFileOnS3 = new FestivalFileOnS3(fileOnS3);
            //S3 엔티티에 페스티벌 연관관계 설정
            festivalFileOnS3.setFestival(festival);
            //DB저장
            festivalFileRepository.save(festivalFileOnS3);
        }
    }

    // s3 삭제
    private void deleteFiles(Festival festival) {
        // 파일정보 불러오기
        List<FestivalFileOnS3> fileOnS3s = festival.getFestivalFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (FestivalFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
                festivalFileRepository.delete(fileOnS3);
            }
        }
    }

    // s3 수정
    private void modifyFiles(Festival festival, List<MultipartFile> files) throws IOException {

        // 기존 파일 삭제
        deleteFiles(festival);

        // 파일 등록
        uploadFiles(files, festival);
    }


    // 페스티벌과 사용자로 좋아요 찾기
    private FestivalLike findFestivalLike(User user, Festival festival) {
        return festivalLikeRepository.findByUserAndFestival(user, festival).orElse(null);
    }


    //태그 생성 및 추가
    private void createFestivalTag(Festival festival, List<TagRequestDto> tags) {
        for (TagRequestDto tag : tags) {
            //존재하는 태그인지 확인 -> 없으면 생성 / 존재하는 태그면 가져오기
            Tag checkedTag = tagService.checkTag(tag);

            //태그 중복 확인 & 태그 페스티벌 연관관계 생성
            tagService.connectTag(festival, checkedTag);
        }
    }

    //예전 페스티벌 태그 정보 삭제
    private void deleteFestivalTag(Festival festival) {
        List<FestivalTag> festivalTags = tagService.findFestivalTagsByFestival(festival);
        for (FestivalTag festivalTag : festivalTags) {
            //페스티벌과 태그 연관관계 삭제
            tagService.deleteFestivalTag(festivalTag);
            //사용되지 않는 태그는 삭제
            tagService.deleteUnusedTag(festivalTag.getTag());
        }
    }
}
