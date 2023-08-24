package com.sparta.lafesta.review.service;

import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.like.reviewLike.entity.ReviewLike;
import com.sparta.lafesta.like.reviewLike.repository.ReviewLikeRepository;
import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.repostiroy.ReviewRepository;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FestivalServiceImpl festivalService;
    private final ReviewLikeRepository reviewLikeRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    // 리뷰 생성
    @Override
    @Transactional
    public ReviewResponseDto createReview(Long festivalId, ReviewRequestDto requestDto, User user) {
        // 주최사, 일반 사용자는 리뷰 작성 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("리뷰를 작성할 수 있는 권한이 없습니다.");
        }

        Festival festival = festivalService.findFestival(festivalId);
        Review review = new Review(festival, requestDto, user);
        reviewRepository.save(review);
        return new ReviewResponseDto(review);
    }

    // 리뷰 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> selectReviews(Long festivalId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Festival festival = festivalService.findFestival(festivalId);
        return reviewRepository.findAllByFestivalOrderByCreatedAtDesc(festival).stream()
                .map(ReviewResponseDto::new).collect(Collectors.toList());
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
    public ReviewResponseDto modifyReview(Long reviewId, ReviewRequestDto requestDto, User user) {
        Review review = findReview(reviewId);

        // 본인이 작성한 글만 수정 가능
        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

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

        reviewRepository.delete(review);
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

    // 리뷰 id로 리뷰 찾기
    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException("선택한 리뷰는 존재하지 않습니다.")
        );
    }

    // 리뷰와 사용자로 좋아요 찾기
    private ReviewLike findReviewLike(User user, Review review) {
        return reviewLikeRepository.findByUserAndReview(user, review).orElse(null);
    }
}
