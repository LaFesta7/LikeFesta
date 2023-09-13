package com.sparta.lafesta.tag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.entity.QFestival;
import com.sparta.lafesta.tag.entity.QFestivalTag;
import com.sparta.lafesta.tag.entity.QTag;
import com.sparta.lafesta.tag.entity.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryCustom {
private final JPAQueryFactory queryFactory;

QTag qTag = new QTag("t");
QFestivalTag qFestivalTag = new QFestivalTag("ft");
QFestival qFestival = new QFestival("f");

public List<Tag> findTagByFestival(Festival festival){
  return queryFactory
      .select(qTag)
      .from(qFestival)
      .leftJoin(qFestival.tags, qFestivalTag)
      .leftJoin(qFestivalTag.tag, qTag)
      .where(qFestival.eq(festival))
      .fetch();
}
}