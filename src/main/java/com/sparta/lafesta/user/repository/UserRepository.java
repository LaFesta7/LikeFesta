package com.sparta.lafesta.user.repository;

import com.sparta.lafesta.follow.entity.UserFollow;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByOrganizerRequest(Boolean organizerRequest, Pageable pageable);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByFollowings(UserFollow followingUser);

    Optional<User> findByFollowers(UserFollow followedUser);
}
