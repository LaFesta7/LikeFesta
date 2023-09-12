package com.sparta.lafesta.like.reviewLike.repository;

import com.sparta.lafesta.like.reviewLike.entity.ReviewLike;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository <ReviewLike, Long> {
    Optional<ReviewLike> findByUserAndReview (User user, Review review);
}
