package com.sparta.lafesta.like.commentLike.repository;

import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.like.commentLike.entity.CommentLike;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository <CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
