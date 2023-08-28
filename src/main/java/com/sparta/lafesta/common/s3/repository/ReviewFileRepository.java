package com.sparta.lafesta.common.s3.repository;

import com.sparta.lafesta.common.s3.entity.ReviewFileOnS3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFileRepository extends JpaRepository<ReviewFileOnS3, Long> {
}
