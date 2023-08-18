package com.sparta.lafesta.review.service;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.repostiroy.ReviewRepository;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FestivalServiceImpl festivalService;

    // 리뷰 생성
    @Override
    @Transactional
    public ReviewResponseDto createReview(Long festivalId, ReviewRequestDto requestDto, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
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
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Review review = findReview(reviewId);
        review.modify(requestDto);
        return new ReviewResponseDto(review);
    }

    // 페스티벌 삭제
    @Override
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Review review = findReview(reviewId);
        reviewRepository.delete(review);
    }

    // 리뷰 id로 리뷰 찾기
    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException("선택한 리뷰는 존재하지 않습니다.")
        );
    }
}
