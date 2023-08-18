package com.sparta.lafesta.comment.service;

import com.sparta.lafesta.comment.dto.CommentRequestDto;
import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface CommentService {
    /**
     * @param reviewId
     * @param requestDto
     * @param user
     * @return 댓글 추가 결과
     */
    CommentResponseDto createComment(Long reviewId, CommentRequestDto requestDto, User user);

    /**
     * @param reviewId
     * @param user
     * @return 전체 댓글 조회 결과
     */
    List<CommentResponseDto> selectComments(Long reviewId, User user);

    /**
     * @param commentId
     * @param requestDto
     * @param user
     * @return
     */
    CommentResponseDto modifyComment(Long commentId, CommentRequestDto requestDto, User user);

    /**
     * @param commentId
     * @param user
     */
    void deleteComment(Long commentId, User user);
}
