package com.sparta.lafesta.lineUp.repository;

import com.sparta.lafesta.lineUp.entity.LineUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface LineUpRepository extends JpaRepository<LineUp, Long> {
    Collection<Object> findByFestivalId(Long festivalId);
}
