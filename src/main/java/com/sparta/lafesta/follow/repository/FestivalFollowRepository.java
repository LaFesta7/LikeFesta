package com.sparta.lafesta.follow.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FestivalFollowRepository extends JpaRepository<FestivalFollow, Long> {
    Optional<FestivalFollow> findByFollowedFestivalAndFollowingFestivalUser(Festival followedFestival, User followingFestivalUser);

    List<FestivalFollow> findAllByFollowingFestivalUser(User follower);
}
