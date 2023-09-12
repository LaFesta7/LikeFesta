package com.sparta.lafesta.review.repostiroy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.like.reviewLike.entity.QReviewLike;
import com.sparta.lafesta.review.entity.QReview;
import com.sparta.lafesta.review.entity.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  QReview qReview = new QReview("r");
  QReviewLike qReviewLike = new QReviewLike("l");

  public List<Review> findTop3Review(){
    return queryFactory
        .select(qReview)
        .from(qReview)
        .leftJoin(qReview.reviewLikes, qReviewLike)
        .groupBy(qReview.id)
        .orderBy(qReviewLike.review.count().desc())
        .limit(3)
        .fetch();
  }
}
