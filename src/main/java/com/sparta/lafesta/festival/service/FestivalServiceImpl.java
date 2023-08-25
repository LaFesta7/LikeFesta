package com.sparta.lafesta.festival.service;


import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.common.s3.repository.FestivalFileRepository;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.like.festivalLike.repository.FestivalLikeRepository;
import com.sparta.lafesta.notification.dto.FestivalReminderResponseDto;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;
    private final FestivalLikeRepository festivalLikeRepository;
    private final S3UploadService s3UploadService;
    private final FestivalFileRepository festivalFileRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private final String FESTIVAL_FOLDER_NAME = "festival";


    // 페스티벌 등록
    @Override
    @Transactional
    public FestivalResponseDto createFestival(FestivalRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {

        // 허가되지 않은 주최사, 일반 사용자 접근 시 예외처리
        if (user.getRole().getAuthority().equals("ROLE_USER")) {
            throw new UnauthorizedException("해당 요청에 접근할 수 없습니다.");
        }
        Festival festival = new Festival(requestDto, user);

        //festival DB 저장
        festivalRepository.save(festival);

        // 첨부파일업로드 -> 이후 업로드된 파일의 url주소를 festival객체에 담아줄 예정.
        uploadFiles(files, festival);

        return new FestivalResponseDto(festival);
    }


    // 페스티벌 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFestivals() {
        return festivalRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(FestivalResponseDto::new).collect(Collectors.toList());
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
    public FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {
        Festival festival = findFestival(festivalId);

        // 주최사는 본인이 작성한 글만 수정 가능
        if (user.getRole().getAuthority().equals("ROLE_ORGANIZER") && !festival.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        // 관리자는 관리자가 작성한 글만 수정 가능
        if (user.getRole().getAuthority().equals("ROLE_ADMIN") && festival.getUser().getRole().getAuthority().equals("ROLE_ORGANIZER")) {
            throw new UnauthorizedException("주최사가 작성한 글은 관리자 권한으로 수정할 수 없습니다.");
        }

        //첨부파일 변경
        modifyFiles(festival, files);

        //페스티벌 정보 변경
        festival.modify(requestDto);

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

        festivalRepository.delete(festival);
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

    // 알림을 보낼 페스티벌 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<FestivalReminderResponseDto> getFestivalReminders() {
        // 페스티벌 개최 당일, 1일 전, 7일 전 발송
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate sevenDaysAfter = today.plusDays(7);

        List<LocalDate> dateRanges = Arrays.asList(today, tomorrow, sevenDaysAfter);

        List<Festival> reminderFestivals = dateRanges.stream()
                .map(date -> {
                    LocalDateTime startOfDay = date.atStartOfDay();
                    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                    return festivalRepository.findAllByOpenDateBetween(startOfDay, endOfDay);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return reminderFestivals.stream().map(FestivalReminderResponseDto::new).toList();
    }

    // 페스티벌 id로 페스티벌 찾기
    private Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌은 존재하지 않습니다.")
        );
    }


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

    private void deleteFiles(Festival festival) {
        // 파일정보 불러오기
        List<FestivalFileOnS3> fileOnS3s = festival.getFestivalFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (FestivalFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
            }
        }
    }

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
}
