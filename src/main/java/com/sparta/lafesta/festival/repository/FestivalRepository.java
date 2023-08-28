package com.sparta.lafesta.festival.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findAllByOrderByCreatedAtDesc();

    Optional<Festival> findByFestivalFollowers(FestivalFollow festivalFollow);

    List<Festival> findAllByOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Festival> findAllByReservationOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Festival> findAllByEndDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
