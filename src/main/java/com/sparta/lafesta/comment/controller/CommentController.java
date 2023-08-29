package com.sparta.lafesta.comment.controller;

import com.sparta.lafesta.comment.dto.CommentRequestDto;
import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.comment.service.CommentService;
import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "페스티벌 리뷰 댓글 관련 API", description = "페스티벌 리뷰 댓글 관련 API 입니다.")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/festivals/{festivalId}/reviews/{reviewId}/comments")
    @Operation(summary = "페스티벌 리뷰 댓글 작성", description = "@PathVariable을 통해 reviewId를 받아와, 해당 위치에 페스티벌 리뷰에 댓글을 작성합니다. Dto를 통해 정보를 받아와 댓글을 생성할 때 해당 정보를 저장합니다.")
    public ResponseEntity<ApiResponseDto> createComment(
            @Parameter(name = "reviewId", description = "댓글을 생성할 review의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "댓글을 생성할 때 필요한 정보") @RequestBody CommentRequestDto requestDto,
            @Parameter(description = "권한 확인 및 작성자 정보를 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto result = commentService.createComment(reviewId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "댓글을 추가했습니다."));
    }

    @GetMapping("/festivals/{festivalId}/reviews/{reviewId}/comments")
    @Operation(summary = "페스티벌 리뷰 댓글 전체 조회", description = "페스티벌 리뷰 댓글을 전체 조회합니다.")
    public ResponseEntity<List<CommentResponseDto>> selectComments(
            @Parameter(name = "reviewId", description = "댓글을 조회할 review의 id", in = ParameterIn.PATH) @PathVariable Long reviewId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "comment 페이징 처리에 필요한 기본 설정")@PageableDefault(size=20, sort="createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        List<CommentResponseDto> results = commentService.selectComments(reviewId, userDetails.getUser(), pageable);
        return ResponseEntity.ok().body(results);
    }

    @PutMapping("/festivals/{festivalId}/reviews/{reviewId}/comments/{commentId}")
    @Operation(summary = "페스티벌 리뷰 댓글 내용 수정", description = "@PathVariable을 통해 commentId를 받아와, 해당 댓글의 내용을 수정합니다. Dto를 통해 정보를 가져옵니다.")
    public ResponseEntity<CommentResponseDto> modifyComment(
            @Parameter(name = "commentId", description = "수정할 comment의 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(description = "comment를 수정할 때 필요한 정보") @RequestBody CommentRequestDto requestDto,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto result = commentService.modifyComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/festivals/{festivalId}/reviews/{reviewId}/comments/{commentId}")
    @Operation(summary = "페스티벌 리뷰 댓글 삭제", description = "@PathVariable을 통해 commentId를 받아와, 해당 댓글을 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteComment(
            @Parameter(name = "commentId", description = "삭제할 comment의 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "댓글 삭제 완료"));
    }

    @PostMapping("/festivals/{festivalId}/reviews/{reviewId}/comments/{commentId}/likes")
    @Operation(summary = "페스티벌 리뷰 댓글 좋아요 추가", description = "페스티벌 리뷰 댓글에 좋아요를 추가합니다.")
    public ResponseEntity<ApiResponseDto> createCommentLike(
            @Parameter(name = "commentId", description = "좋아요를 추가할 댓글의 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto result = commentService.createCommentLike(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.CREATED.value(), "좋아요를 추가했습니다. 좋아요 수: " + result.getLikeCnt()));
    }

    @DeleteMapping("/festivals/{festivalId}/reviews/{reviewId}/comments/{commentId}/likes-cancel")
    @Operation(summary = "페스티벌 리뷰 댓글 좋아요 취소", description = "페스티벌 리뷰 댓글에 좋아요를 취소합니다.")
    public ResponseEntity<ApiResponseDto> deleteCommentLike(
            @Parameter(name = "commentId", description = "좋아요를 취소할 댓글의 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto result = commentService.deleteCommentLike(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "좋아요를 취소했습니다. 좋아요 수: " + result.getLikeCnt()));
    }
}
