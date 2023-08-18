package com.sparta.lafesta.comment.repository;

import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByReviewOrderByCreatedAtDesc(Review review);
}
