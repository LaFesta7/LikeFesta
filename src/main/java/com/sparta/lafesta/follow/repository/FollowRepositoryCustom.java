package com.sparta.lafesta.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
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

  public List<SelectUserResponseDto> findAllFollowers(User user, Pageable pageable){
    return queryFactory
        .select(qUser2)
        .from(qUser)
        .leftJoin(qUser.followings, qUserFollow)
        .leftJoin(qUserFollow.followingUser, qUser2)
        .offset(pageable.getOffset())
        .where(qUser.eq(user))
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(SelectUserResponseDto::new).toList();
  }

//  public List<SelectUserResponseDto> findAllFollowings(User user){
//
//  }

}
