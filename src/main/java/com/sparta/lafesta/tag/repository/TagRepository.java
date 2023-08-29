package com.sparta.lafesta.tag.repository;

import com.sparta.lafesta.tag.entity.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAllBy();

  Optional<Tag> findByTitle(String tag);
}
