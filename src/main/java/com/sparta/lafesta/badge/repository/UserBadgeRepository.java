package com.sparta.lafesta.badge.repository;

import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findAllByUser(User selectUser);
}
