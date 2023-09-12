package com.sparta.lafesta.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.follow.entity.QUserFollow;
import com.sparta.lafesta.user.entity.QUser;
import com.sparta.lafesta.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  QUser qUser = new QUser("u");
  QUserFollow qUserFollow = new QUserFollow("f");

  public List<User> findTop3User(){
    return queryFactory
        .select(qUser)
        .from(qUser)
        .leftJoin(qUser.followings, qUserFollow)
        .groupBy(qUser.id)
        .orderBy(qUserFollow.count().desc())
        .limit(3)
        .fetch();
  }
}