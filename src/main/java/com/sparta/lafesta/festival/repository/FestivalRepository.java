package com.sparta.lafesta.festival.repository;

import com.sparta.lafesta.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findAllByOrderByCreatedAtDesc();

    List<Festival> findAllByOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
