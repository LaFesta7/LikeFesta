package com.sparta.lafesta.comment.service;

import com.sparta.lafesta.comment.dto.CommentRequestDto;
import com.sparta.lafesta.comment.dto.CommentResponseDto;
import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.comment.repository.CommentRepository;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.like.commentLike.entity.CommentLike;
import com.sparta.lafesta.like.commentLike.repository.CommentLikeRepository;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.review.service.ReviewServiceImpl;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ReviewServiceImpl reviewService;
    private final CommentLikeRepository commentLikeRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    // 댓글 생성
    @Override
    @Transactional
    public CommentResponseDto createComment(Long reviewId, CommentRequestDto requestDto, User user) {
        // 주최사, 일반 사용자는 댓글 작성 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("댓글을 작성할 수 있는 권한이 없습니다.");
        }

        Review review = reviewService.findReview(reviewId);
        Comment comment = new Comment(review, requestDto, user);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> selectComments(Long reviewId, User user, Pageable pageable) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Review review = reviewService.findReview(reviewId);
        return commentRepository.findAllByReview(review, pageable).stream()
                .map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // 댓글 내용 수정
    @Override
    @Transactional
    public CommentResponseDto modifyComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);

        // 본인이 작성한 글만 수정 가능
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }


        comment.modify(requestDto);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        // 주최사, 일반 사용자는 본인이 작성한 글만 삭제 가능, 관리자는 모든 글 삭제 가능
        if (!comment.getUser().getId().equals(user.getId())
                && !user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("해당 리뷰를 삭제할 수 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // 댓글 좋아요 추가
    @Override
    @Transactional
    public CommentResponseDto createCommentLike(Long commentId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        Comment comment = findComment(commentId);
        // 좋아요를 이미 누른 경우 오류 반환
        if (findCommentLike(user, comment) != null) {
            throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 댓글에 좋아요 추가
        commentLikeRepository.save(new CommentLike(user, comment));

        return new CommentResponseDto(comment);
    }

    // 댓글 좋아요 취소
    @Override
    @Transactional
    public CommentResponseDto deleteCommentLike(Long commentId, User user) {
        // 주최사, 일반 사용자는 좋아요 추가 가능(관리자 불가)
        if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("좋아요에 관한 권한이 없습니다.");
        }

        CommentResponseDto response = transactionTemplate.execute(status -> {
            Comment comment = findComment(commentId);
            // 좋아요를 누르지 않은 경우 오류 반환
            if (findCommentLike(user, comment) == null) {
                throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
            }
            // 오류가 나지 않을 경우 해당 댓글에 좋아요 취소
            commentLikeRepository.delete(findCommentLike(user, comment));

            // 여기에서 커밋을 수행 (트랜잭션 내에서 커밋 또는 롤백을 수행할 수 있음)
            status.flush();

            // CommentResponseDto 생성 후 반환
            return new CommentResponseDto(comment);
        });

        // 위에서 커밋이 수행되었으므로 CommentResponseDto에서 새로운 likeCnt를 가져올 수 있음
        return response;
    }

    // 댓글 id로 댓글 찾기
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
    }

    // 리뷰와 사용자로 좋아요 찾기
    private CommentLike findCommentLike(User user, Comment comment) {
        return commentLikeRepository.findByUserAndComment(user, comment).orElse(null);
    }
}
