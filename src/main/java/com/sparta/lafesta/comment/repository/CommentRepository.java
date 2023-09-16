package com.sparta.lafesta.comment.repository;

import com.sparta.lafesta.comment.entity.Comment;
import com.sparta.lafesta.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByReview(Review review, Pageable pageable);
}
