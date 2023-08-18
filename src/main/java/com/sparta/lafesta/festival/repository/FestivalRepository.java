package com.sparta.lafesta.festival.repository;

import com.sparta.lafesta.festival.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findAllByOrderByCreatedAtDesc();
}
