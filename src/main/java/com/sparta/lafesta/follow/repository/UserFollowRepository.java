package com.sparta.lafesta.follow.repository;

import com.sparta.lafesta.follow.entity.UserFollow;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Optional<UserFollow> findByFollowedUserAndFollowingUser(User followedUser, User followingUser);

    List<UserFollow> findAllByFollowedUser(User followedUser);
}
