package com.sparta.lafesta.review.repostiroy;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByFestival(Festival festival, Pageable pageable);

    boolean existsByUserAndFestival(User user, Festival festival);

    List<Review> findAllByUser(User user);
    Page<Review> findAllByUser(User user, Pageable pageable);
}
