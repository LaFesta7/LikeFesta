package com.sparta.lafesta.common.s3.repository;

import com.sparta.lafesta.common.s3.entity.FestivalFileOnS3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalFileRepository extends JpaRepository<FestivalFileOnS3, Long> {
}
