package com.sparta.lafesta.like.festivalLike.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface FestivalLikeRepository extends JpaRepository <FestivalLike, Long> {
    Optional<FestivalLike> findByUserAndFestival(User user, Festival festival);
}
