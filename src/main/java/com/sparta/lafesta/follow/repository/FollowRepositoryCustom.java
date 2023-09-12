package com.sparta.lafesta.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.QFestival;
import com.sparta.lafesta.follow.entity.QFestivalFollow;
import com.sparta.lafesta.follow.entity.QUserFollow;
import com.sparta.lafesta.user.dto.SelectUserResponseDto;
import com.sparta.lafesta.user.entity.QUser;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryCustom {
  private final JPAQueryFactory queryFactory;
QUser qUser = new QUser("u");
QUserFollow qUserFollow = new QUserFollow("f");
QUser qUser2 = new QUser("u2");
QFestival qFestival = new QFestival("fs");
QFestivalFollow qFestivalFollow = new QFestivalFollow("ff");

  public List<SelectUserResponseDto> findAllFollowers(User user, Pageable pageable){
    return queryFactory
        .select(qUser2)
        .from(qUser)
        .leftJoin(qUser.followings, qUserFollow)
        .leftJoin(qUserFollow.followingUser, qUser2)
        .offset(pageable.getOffset())
        .where(qUser.eq(user))
        .orderBy(qUserFollow.createdAt.desc())
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(SelectUserResponseDto::new).toList();
  }

  public List<SelectUserResponseDto> findAllFollowings(User user, Pageable pageable){
    return queryFactory
        .select(qUser2)
        .from(qUser)
        .leftJoin(qUser.followers, qUserFollow)
        .leftJoin(qUserFollow.followedUser, qUser2)
        .offset(pageable.getOffset())
        .where(qUser.eq(user))
        .orderBy(qUserFollow.createdAt.desc())
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(SelectUserResponseDto::new).toList();
  }

  public List<FestivalResponseDto> findAllFollowFestival(User user, Pageable pageable){
    return queryFactory
        .select(qFestival)
        .from(qFestival)
        .leftJoin(qFestival.festivalFollowers, qFestivalFollow)
        .leftJoin(qFestivalFollow.followingFestivalUser, qUser)
        .offset(pageable.getOffset())
        .where(qUser.eq(user))
        .orderBy(qFestivalFollow.createdAt.desc())
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(FestivalResponseDto::new).toList();
  }
}
