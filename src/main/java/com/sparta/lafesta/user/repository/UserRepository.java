package com.sparta.lafesta.user.repository;

import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);

    Optional<User> findByFollowings(UserFollow followingUser);

    Optional<User> findByFollowers(UserFollow followedUser);
}
