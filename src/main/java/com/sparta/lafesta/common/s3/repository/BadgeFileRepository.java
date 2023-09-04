package com.sparta.lafesta.common.s3.repository;

import com.sparta.lafesta.common.s3.entity.BadgeFileOnS3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeFileRepository extends JpaRepository<BadgeFileOnS3, Long> {
}
