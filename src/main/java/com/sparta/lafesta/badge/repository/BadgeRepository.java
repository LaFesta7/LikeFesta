package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByUserBadges(UserBadge userBadge);
}
