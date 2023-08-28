package com.sparta.lafesta.tag.repository;

import com.sparta.lafesta.tag.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findAllBy();

}
