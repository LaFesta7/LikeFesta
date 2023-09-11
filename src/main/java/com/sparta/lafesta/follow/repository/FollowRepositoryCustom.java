package com.sparta.lafesta.follow.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
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

//팔로워 목록 조회
  public List<SelectUserResponseDto> findAllFollowers(User user, Pageable pageable){
    return queryFactory
        .select(qUser2)
        .from(qUser)
        .leftJoin(qUser.followings, qUserFollow) //로그인한 유저와 팔로우 연관관계 조인
        .leftJoin(qUserFollow.followingUser, qUser2) //팔로우 연관관계와 팔로우한 유저 조인
        .offset(pageable.getOffset()) //페이징의 페이지 번호 정보
        .where(qUser.eq(user)) //조건 - 로그인한 유저가 팔로우 대상일 것
        .orderBy(qUserFollow.createdAt.desc()) //팔로우한 시간 순으로 정렬
        .limit(pageable.getPageSize()) //페이징의 한 페이지에 보여줄 유저 수
        .fetch() //리스트로 반환
        .stream().map(SelectUserResponseDto::new).toList();
  }

  //팔로잉 목록 조회
  public List<SelectUserResponseDto> findAllFollowings(User user, Pageable pageable, Long lastFollowId){ //팔로잉 목록 조회 - no offset 적용하기
    return queryFactory
        .select(qUser2)
        .from(qUser)
        .leftJoin(qUser.followers, qUserFollow)
        .leftJoin(qUserFollow.followedUser, qUser2)
        .where(qUser.eq(user).and(ltFollowId(lastFollowId)))
        .orderBy(qUserFollow.id.desc())
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(SelectUserResponseDto::new).toList();
  }

  //페스티벌 팔로잉 목록 조회
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

//No Offset을 위한 함수
  private BooleanExpression ltFollowId(Long lastFollowId){
    if(lastFollowId == 0l || lastFollowId == null){
      return null;
    }
    return qUser2.id.lt(lastFollowId);
  }
}
