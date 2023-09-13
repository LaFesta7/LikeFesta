package com.sparta.lafesta.comment.service;

import com.sparta.lafesta.comment.dto.CommentRequestDto;
import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    /**
     * 댓글 생성
     * @param reviewId 댓글을 생성할 리뷰의 id
     * @param requestDto 댓글 정보
     * @param user 권한 확인
     * @return 댓글 추가 결과
     */
    CommentResponseDto createComment(Long reviewId, CommentRequestDto requestDto, User user);

    /**
     * 전체 댓글 조회
     * @param reviewId 전체 댓글을 조회할 리뷰의 id
     * @param user 권한 확인
     * @return 전체 댓글 조회 결과
     */
    List<CommentResponseDto> selectComments(Long reviewId, User user, Pageable pageable);

    /**
     * 댓글 수정
     * @param commentId 수정할 댓글의 id
     * @param requestDto 댓글 수정 정보
     * @param user 권한 확인
     * @return 댓글 수정 결과
     */
    CommentResponseDto modifyComment(Long commentId, CommentRequestDto requestDto, User user);

    /**
     * 댓글 삭제
     * @param commentId 수정할 댓글의 id
     * @param user 권한 확인
     */
    void deleteComment(Long commentId, User user);

    /**
     * 선택한 댓글 좋아요 확인
     *
     * @param commentId 좋아요 확인할 페스티벌의 id
     * @param user       권한 확인
     * @return 좋아요 확인 결과
     */
    Boolean selectCommentLike(Long commentId, User user);

    /**
     * 선택한 댓글 좋아요 추가
     * @param commentId 좋아요 추가할 댓글의 id
     * @param user 권한 확인
     * @return 좋아요 추가 결과
     */
    CommentResponseDto createCommentLike(Long commentId, User user);

    /**
     * 선택한 댓글 좋아요 취소
     * @param commentId 좋아요 취소할 댓글의 id
     * @param user 권한 확인
     * @return 좋아요 취소 결과
     */
    CommentResponseDto deleteCommentLike(Long commentId, User user);
}
