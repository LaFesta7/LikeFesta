package com.sparta.lafesta.user.repository;

import com.sparta.lafesta.follow.entity.UserFollow;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByOrganizerRequest(Boolean organizerRequest, Pageable pageable);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByFollowings(UserFollow followingUser);

    Optional<User> findByFollowers(UserFollow followedUser);

    @Query(value = "select u.* \n"
        + "from users u\n"
        + "left join user_follow f on u.id = f.follow_target_id\n"
        + "group by u.id\n"
        + "order by count(f.follow_target_id) desc\n"
        + "limit 3", nativeQuery = true)
    List<User> findTop3User();
}
