package com.sparta.lafesta.tag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.entity.QFestival;
import com.sparta.lafesta.tag.entity.QFestivalTag;
import com.sparta.lafesta.tag.entity.QTag;
import com.sparta.lafesta.tag.entity.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

public Page<FestivalResponseDto> findAllByTag(String title, Pageable pageable){
  List<FestivalResponseDto> results = queryFactory
      .select(qFestival)
      .from(qTag)
      .leftJoin(qTag.festivals, qFestivalTag)
      .leftJoin(qFestivalTag.festival, qFestival)
      .offset(pageable.getOffset())
      .where(qTag.title.eq(title))
      .limit(pageable.getPageSize())
      .fetch()
      .stream().map(FestivalResponseDto::new).toList();

  Long count = queryFactory
      .select(qFestival.count())
      .from(qTag)
      .leftJoin(qTag.festivals, qFestivalTag)
      .leftJoin(qFestivalTag.festival, qFestival)
      .where(qTag.title.eq(title))
      .fetchOne();

  return new PageImpl<>(results, pageable, count);
}
}