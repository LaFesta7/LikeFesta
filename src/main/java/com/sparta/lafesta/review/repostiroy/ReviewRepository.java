package com.sparta.lafesta.review.repostiroy;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByFestivalOrderByCreatedAtDesc(Festival festival);
}
