package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.Badge;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

  List<Badge> findAllBy(Pageable pageable);
}
