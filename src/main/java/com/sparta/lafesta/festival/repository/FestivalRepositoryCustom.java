package com.sparta.lafesta.festival.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.entity.QFestival;
import com.sparta.lafesta.review.entity.QReview;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  QFestival qFestival = new QFestival("f");
  QReview qReviews = new QReview("r");

  public List<Festival> findTop3Festival(){
    return queryFactory
        .select(qFestival)
        .from(qFestival)
        .leftJoin(qFestival.reviews, qReviews)
        .groupBy(qFestival.id)
        .orderBy(qReviews.festival.count().desc())
        .limit(3)
        .fetch();
  }
}
