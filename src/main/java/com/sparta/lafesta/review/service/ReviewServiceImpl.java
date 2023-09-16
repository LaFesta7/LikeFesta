package com.sparta.lafesta.review.service;

import com.sparta.lafesta.badge.service.BadgeService;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.common.s3.S3UploadService;
import com.sparta.lafesta.common.s3.entity.FileOnS3;
import com.sparta.lafesta.common.s3.entity.ReviewFileOnS3;
import com.sparta.lafesta.common.s3.repository.ReviewFileRepository;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.service.FestivalService;
import com.sparta.lafesta.like.reviewLike.entity.ReviewLike;
import com.sparta.lafesta.like.reviewLike.repository.ReviewLikeRepository;
import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.event.ReviewCreatedEventPublisher;
import com.sparta.lafesta.review.repostiroy.ReviewRepository;
import com.sparta.lafesta.review.repostiroy.ReviewRepositoryCustom;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    //CRUD
    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryCustom reviewRepositoryCustom;
    private final FestivalService festivalService;
    private final UserService userService;

    //S3
    private final S3UploadService s3UploadService;
    private final ReviewFileRepository reviewFileRepository;
    private final String REVIEW_FOLDER_NAME = "review";

    //Like
    private final ReviewLikeRepository reviewLikeRepository;

    // 알림
    private final ReviewCreatedEventPublisher eventPublisher;

    // 뱃지
    private final BadgeService badgeService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    // 리뷰 생성
    @Override
    @Transactional
    public ReviewResponseDto createReview(Long festivalId, ReviewRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {
        // 주최사, 일반 사용자는 리뷰 작성 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("리뷰를 작성할 수 있는 권한이 없습니다.");
        }

        Festival festival = festivalService.findFestival(festivalId);

        // 한 페스티벌에 하나의 리뷰만 작성 가능
        // -> 해당 페스티벌에 이미 작성한 리뷰가 있는지 확인
        checkIfUserHasReviewedFestival(user, festival);

        //Review DB에 저장
        Review review = new Review(festival, requestDto, user);
        reviewRepository.save(review);

        //첨부파일 업로드
        if (files != null) {
            uploadFiles(files, review);
        }

        // 뱃지 조건 확인
        badgeService.checkBadgeCondition(user);

        // 이벤트 발생 -> 알림 생성
        eventPublisher.publishReviewCreatedEvent(review);

        return new ReviewResponseDto(review);
    }

    // 리뷰 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> selectReviews(Long festivalId, User user, Pageable pageable) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Festival festival = festivalService.findFestival(festivalId);
        return reviewRepository.findAllByFestival(festival, pageable)
                .map(ReviewResponseDto::new);
    }

    // 특정 유저가 작성한 리뷰 전체 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> selectUserReviews(Long userId, Pageable pageable) {
        User user = userService.findUser(userId);
        return reviewRepository.findAllByUser(user, pageable)
                .map(ReviewResponseDto::new);
    }

    // 리뷰 상세 조회
    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto selectReview(Long reviewId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        return new ReviewResponseDto(findReview(reviewId));
    }

    // 리뷰 내용 수정
    @Override
    @Transactional
    public ReviewResponseDto modifyReview(Long reviewId, ReviewRequestDto requestDto, List<MultipartFile> files, User user) throws IOException {
        Review review = findReview(reviewId);

        // 본인이 작성한 글만 수정 가능
        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        //첨부파일 변경
        if (files != null) {
            modifyFiles(review, files);
        }
        //리뷰 정보 변경
        review.modify(requestDto);
        return new ReviewResponseDto(review);
    }

    // 리뷰 삭제
    @Override
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = findReview(reviewId);

        // 주최사, 일반 사용자는 본인이 작성한 글만 삭제 가능, 관리자는 모든 글 삭제 가능
        if (!review.getUser().getId().equals(user.getId())
                && !user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("해당 리뷰를 삭제할 수 없습니다.");
        }

        //첨부파일 DB에서 삭제
        deleteFiles(review);

        reviewRepository.delete(review);
    }

    // 리뷰 좋아요 확인
    @Override
    @Transactional(readOnly = true)
    public Boolean selectReviewLike(Long reviewId, User user) {
        return findReviewLike(user, findReview(reviewId)) != null;
    }

    // 리뷰 좋아요 추가
    @Override
    @Transactional
    public ReviewResponseDto createReviewLike(Long reviewId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        Review review = findReview(reviewId);
        // 좋아요를 이미 누른 경우 오류 반환
        if (findReviewLike(user, review) != null) {
            throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 리뷰에 좋아요 추가
        reviewLikeRepository.save(new ReviewLike(user, review));

        return new ReviewResponseDto(review);
    }

    // 리뷰 좋아요 취소
    @Override
    @Transactional
    public ReviewResponseDto deleteReviewLike(Long reviewId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        ReviewResponseDto response = transactionTemplate.execute(status -> {
            Review review = findReview(reviewId);
            // 좋아요를 누르지 않은 경우 오류 반환
            if (findReviewLike(user, review) == null) {
                throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
            }
            // 오류가 나지 않을 경우 해당 리뷰에 좋아요 취소
            reviewLikeRepository.delete(findReviewLike(user, review));

            // 여기에서 커밋을 수행 (트랜잭션 내에서 커밋 또는 롤백을 수행할 수 있음)
            status.flush();

            // ReviewResponseDto 생성 후 반환
            return new ReviewResponseDto(review);
        });

        // 위에서 커밋이 수행되었으므로 ReviewResponseDto에서 새로운 likeCnt를 가져올 수 있음
        return response;
    }

    //리뷰 랭킹 조회
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> selectReviewRanking(User user){
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        return reviewRepositoryCustom.findTop3Review().stream()
            .map(ReviewResponseDto::new).toList();
    }

    // 해당 페스티벌에 이미 작성한 리뷰 존재하는지 확인
    public void checkIfUserHasReviewedFestival(User user, Festival festival) {
        boolean hasReviewed = reviewRepository.existsByUserAndFestival(user, festival);
        if (hasReviewed) {
            throw new IllegalArgumentException("해당 페스티벌에 대한 리뷰를 이미 작성하였습니다.");
        }
    }

    // 리뷰 id로 리뷰 찾기
    @Override
    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException("선택한 리뷰는 존재하지 않습니다.")
        );
    }

    // s3 업로드
    private void uploadFiles(List<MultipartFile> files, Review review) throws IOException {
        List<FileOnS3> fileOnS3s = new ArrayList<>();
        if (files != null) {
            fileOnS3s = s3UploadService.putObjects(files, REVIEW_FOLDER_NAME, review.getId());
        }

        // FielOnS3를 Festival로 변환
        for (FileOnS3 fileOnS3 : fileOnS3s) {
            //페스티벌 파일 S3 엔티티로 변환생성
            ReviewFileOnS3 reviewFileOnS3 = new ReviewFileOnS3(fileOnS3);
            //S3 엔티티에 페스티벌 연관관계 설정
            reviewFileOnS3.setReview(review);
            //DB저장
            reviewFileRepository.save(reviewFileOnS3);
        }
    }

    // s3 삭제
    private void deleteFiles(Review review) {
        // 파일정보 불러오기
        List<ReviewFileOnS3> fileOnS3s = review.getReviewFileOnS3s();

        // 파일 삭제 실행
        if (!fileOnS3s.isEmpty()) { // 파일이 있다면 실행
            for (ReviewFileOnS3 fileOnS3 : fileOnS3s) {
                s3UploadService.deleteFile(fileOnS3.getKeyName());
                reviewFileRepository.delete(fileOnS3);
            }
        }
    }

    // s3 수정
    private void modifyFiles(Review review, List<MultipartFile> files) throws IOException {

        // 기존 파일 삭제
        deleteFiles(review);

        // 파일 등록
        uploadFiles(files, review);
    }



    // 리뷰와 사용자로 좋아요 찾기
    private ReviewLike findReviewLike(User user, Review review) {
        return reviewLikeRepository.findByUserAndReview(user, review).orElse(null);
    }
}
