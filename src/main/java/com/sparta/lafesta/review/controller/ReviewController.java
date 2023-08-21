package com.sparta.lafesta.review.controller;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.review.dto.ReviewRequestDto;
import com.sparta.lafesta.review.dto.ReviewResponseDto;
import com.sparta.lafesta.review.service.ReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "페스티벌 리뷰 관련 API", description = "페스티벌 리뷰 관련 API 입니다.")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @PostMapping("/festivals/{festivalId}/reviews")
    @Operation(summary = "페스티벌 리뷰 작성", description = "@PathVariable을 통해 festivalId를 받아와, 해당 위치에 페스티벌 리뷰를 작성합니다. Dto를 통해 정보를 받아와 review를 생성할 때 해당 정보를 저장합니다.")
    public ResponseEntity<ApiResponseDto> createReview(
            @Parameter(name = "festivalId", description = "리뷰를 생성할 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "리뷰를 생성할 때 필요한 정보") @RequestBody ReviewRequestDto requestDto,
            @Parameter(description = "권한 확인 및 작성자 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponseDto result = reviewService.createReview(festivalId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), result.getTitle()+"를 추가했습니다."));
    }

    @GetMapping("/festivals/{festivalId}/reviews")
    @Operation(summary = "페스티벌 리뷰 전체 조회", description = "페스티벌 리뷰를 전체 조회합니다.")
    public ResponseEntity<List<ReviewResponseDto>> selectReviews(
            @Parameter(name = "festivalId", description = "리뷰를 조회할 festival의 id", in = ParameterIn.PATH) @PathVariable Long festivalId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ReviewResponseDto> results = reviewService.selectReviews(festivalId, userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/festivals/{festivalId}/reviews/{reviewId}")
    @Operation(summary = "페스티벌 리뷰 상세 조회", description = "@PathVariable을 통해 reviewId를 받아와, 해당 review을 상세 조회합니다.")
    public ResponseEntity<ReviewResponseDto> selectReview(
            @Parameter(name = "reviewId", description = "선택한 review의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponseDto result = reviewService.selectReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/festivals/{festivalId}/reviews/{reviewId}")
    @Operation(summary = "페스티벌 리뷰 내용 수정", description = "@PathVariable을 통해 reviewId를 받아와, 해당 리뷰의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<ReviewResponseDto> modifyReview(
            @Parameter(name = "reviewId", description = "수정할 review의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "review를 수정할 때 필요한 정보") @RequestBody ReviewRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponseDto result = reviewService.modifyReview(reviewId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/festivals/{festivalId}/reviews/{reviewId}")
    @Operation(summary = "페스티벌 리뷰 삭제", description = "@PathVariable을 통해 reviewId를 받아와, 해당 리뷰를 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteReview(
            @Parameter(name = "reviewId", description = "삭제할 review의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "리뷰 삭제 완료"));
    }

    @PostMapping("/festivals/{festivalId}/reviews/{reviewId}/likes")
    @Operation(summary = "페스티벌 리뷰 좋아요 추가", description = "페스티벌 리뷰에 좋아요를 추가합니다.")
    public ResponseEntity<ApiResponseDto> createReviewLike(
            @Parameter(name = "reviewId", description = "좋아요를 추가할 리뷰의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponseDto result = reviewService.createReviewLike(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "좋아요를 추가했습니다.\n좋아요 수: " + result.getLikeCnt()));
    }

    @DeleteMapping("/festivals/{festivalId}/reviews/{reviewId}/likes-cancel")
    @Operation(summary = "페스티벌 리뷰 좋아요 취소", description = "페스티벌 리뷰에 좋아요를 취소합니다.")
    public ResponseEntity<ApiResponseDto> deleteReviewLike(
            @Parameter(name = "reviewId", description = "좋아요를 취소할 리뷰의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponseDto result = reviewService.deleteReviewLike(reviewId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "좋아요를 취소했습니다.\n좋아요 수: " + result.getLikeCnt()));
    }
}
