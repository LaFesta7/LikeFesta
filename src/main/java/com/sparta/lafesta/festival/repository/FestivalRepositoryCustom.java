package com.sparta.lafesta.festival.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
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
  QReview reviews = new QReview("r");

//  public List<FestivalResponseDto> practice(){
//    return queryFactory
//        .select(qFestival)
//        .from(qFestival)
//        .fetch()
//        .stream()
//        .map(FestivalResponseDto::new).toList();
//  }
NumberPath<Long> aliasQuantity = Expressions.numberPath(Long.class, "aliasQuantity");

  public List<Festival> findTop3Festival(){

    reviews.count().as(aliasQuantity);

    return queryFactory
        .select(qFestival)
        .from(qFestival)
        .leftJoin(qFestival.reviews, reviews)
        .groupBy(qFestival.id)
//        .orderBy(aliasQuantity.desc())
        .limit(3)
        .fetch();
  }

}
