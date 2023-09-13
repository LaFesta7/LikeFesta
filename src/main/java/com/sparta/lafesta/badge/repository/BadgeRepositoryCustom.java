package com.sparta.lafesta.badge.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.badge.dto.UserBadgeResponseDto;
import com.sparta.lafesta.badge.entity.QUserBadge;
import com.sparta.lafesta.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BadgeRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  QUserBadge qUserBadge = new QUserBadge("ub");

  public List<UserBadgeResponseDto> findAllByUser(User selectUser, Pageable pageable, Long lastBadge){
    return queryFactory
        .selectFrom(qUserBadge)
        .where(qUserBadge.user.eq(selectUser).and(ltUserBadgeId(lastBadge)))
        .orderBy(qUserBadge.badge.id.desc())
        .limit(pageable.getPageSize())
        .fetch()
        .stream().map(UserBadgeResponseDto::new).toList();
  }

  private BooleanExpression ltUserBadgeId(Long lastBadge){
    if(lastBadge == 0l || lastBadge == null){
      return null;
    }
    return qUserBadge.badge.id.lt(lastBadge);
  }
}
