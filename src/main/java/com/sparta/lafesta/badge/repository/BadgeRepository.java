package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.Badge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

  Page<Badge> findAllBy(Pageable pageable);
}
