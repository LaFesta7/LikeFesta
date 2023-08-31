package com.sparta.lafesta.festival.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.tag.entity.FestivalTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

    Optional<Festival> findByFestivalFollowers(FestivalFollow festivalFollow);

    List<Festival> findAllByOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Page<Festival> findAllBy(Pageable pageable);

    List<Festival> findAllByReservationOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Festival> findAllByEndDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Festival> findByTags(FestivalTag tags);

    @Query(value = "select f.* \n"
        + "from festivals f\n"
        + "left join reviews r on f.id = r.festival_id\n"
        + "group by f.id\n"
        + "order by count(r.festival_id) desc\n"
        + "limit 3", nativeQuery = true)
    List<Festival> findTop3Festival();
}
