package com.sparta.lafesta.review.service;

import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    /**
     * 리뷰 생성
     *
     * @param festivalId 리뷰를 추가할 페스티벌의 id
     * @param requestDto 추가할 리뷰의 정보
     * @param files      첨부파일
     * @param user       권한 확인
     * @return 리뷰 추가 결과
     */
    ReviewResponseDto createReview(Long festivalId, ReviewRequestDto requestDto, List<MultipartFile> files, User user) throws IOException;

    /**
     * 전체 리뷰 조회
     * @param festivalId 전체 리뷰를 조회할 페스티벌의 id
     * @param user 권한 확인
     * @return 전체 리뷰 조회 결과
     */
    Page<ReviewResponseDto> selectReviews(Long festivalId, User user, Pageable pageable);

    /**
     * 특정 유저가 작성한 리뷰 조회
     *
     * @param userId 조회할 유저의 id
     * @param pageable 페이징 처리를 위한 정보
     * @return 특정 유저가 작성한 리뷰 조회 결과
     */
    Page<ReviewResponseDto> selectUserReviews(Long userId, Pageable pageable);

    /**
     * 리뷰 상세 조회
     * @param reviewId 조회할 리뷰의 id
     * @param user 권한 확인
     * @return 리뷰 상세 조회 결과
     */
    ReviewResponseDto selectReview(Long reviewId, User user);

    /**
     * 리뷰 수정
     *
     * @param reviewId   수정할 리뷰의 id
     * @param requestDto 리뷰 수정할 정보
     * @param files      첨부파일
     * @param user       권한 확인
     * @return 리뷰 수정 결과
     */
    ReviewResponseDto modifyReview(Long reviewId, ReviewRequestDto requestDto, List<MultipartFile> files, User user) throws IOException;

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰의 id
     * @param user 권한 확인
     */
    void deleteReview(Long reviewId, User user);

    /**
     * 선택한 리뷰 좋아요 확인
     *
     * @param reviewId 좋아요 확인할 페스티벌의 id
     * @param user       권한 확인
     * @return 좋아요 확인 결과
     */
    Boolean selectReviewLike(Long reviewId, User user);

    /**
     * 선택한 리뷰 좋아요 추가
     * @param reviewId 좋아요 추가할 리뷰의 id
     * @param user 권한 확인
     * @return 좋아요 추가 결과
     */
    ReviewResponseDto createReviewLike(Long reviewId, User user);

    /**
     * 선택한 리뷰 좋아요 취소
     * @param reviewId 좋아요 취소할 리뷰의 id
     * @param user 권한 확인
     * @return 좋아요 취소 결과
     */
    ReviewResponseDto deleteReviewLike(Long reviewId, User user);

    /**
     * 리뷰 랭킹 조회
     *
     * @param user 회원 권한 확인
     * @return 리뷰 랭킹 조회 결과
     */
    List<ReviewResponseDto> selectReviewRanking(User user);

    /**
     * 리뷰 가져오기
     * @param reviewId 가져올 리뷰의 id
     * @return 가져온 리뷰
     */
    Review findReview(Long reviewId);
}
