package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
