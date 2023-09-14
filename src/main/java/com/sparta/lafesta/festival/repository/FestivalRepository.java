package com.sparta.lafesta.festival.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

    List<Festival> findAllByOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Page<Festival> findAllBy(Pageable pageable);

    List<Festival> findAllByReservationOpenDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Festival> findAllByEndDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Festival> findByTags(FestivalTag tags);

    List<Festival> findByTitleContaining(String keyword, Pageable pageable);

    Page<Festival> findAllByUser(User user, Pageable pageable);
}
