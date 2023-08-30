package com.sparta.lafesta.tag.repository;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.tag.entity.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalTagRepository extends JpaRepository<FestivalTag, Long> {

  Optional<FestivalTag> findByTagAndFestival(Tag tag, Festival festival);

  List<FestivalTag> findAllByTag(Tag tag);

  List<FestivalTag> findAllByFestival(Festival festival);
}
