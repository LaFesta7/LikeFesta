package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.Badge;
import com.sparta.lafesta.badge.entity.BadgeTag;
import com.sparta.lafesta.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeTagRepository extends JpaRepository<BadgeTag, Long> {
    Optional<BadgeTag> findByBadgeAndTag(Badge badge, Tag tag);

}
