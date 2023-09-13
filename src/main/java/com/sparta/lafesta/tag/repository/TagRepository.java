package com.sparta.lafesta.tag.repository;

import com.sparta.lafesta.tag.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Page<Tag> findAllBy(Pageable pageable);

    Optional<Tag> findByTitle(String tag);
}
