package com.sparta.lafesta.review.service;

import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface ReviewService {
    /**
     * @param festivalId
     * @param requestDto
     * @param user
     * @return 리뷰 추가 결과
     */
    ReviewResponseDto createReview(Long festivalId, ReviewRequestDto requestDto, User user);

    /**
     * @param festivalId
     * @param user
     * @return 전체 리뷰 조회 결과
     */
    List<ReviewResponseDto> selectReviews(Long festivalId, User user);

    /**
     * @param reviewId
     * @param user
     * @return 리뷰 상세 조회 결과
     */
    ReviewResponseDto selectReview(Long reviewId, User user);

    /**
     * @param reviewId
     * @param requestDto
     * @param user
     * @return
     */
    ReviewResponseDto modifyReview(Long reviewId, ReviewRequestDto requestDto, User user);

    /**
     * @param reviewId
     * @param user
     */
    void deleteReview(Long reviewId, User user);
}
