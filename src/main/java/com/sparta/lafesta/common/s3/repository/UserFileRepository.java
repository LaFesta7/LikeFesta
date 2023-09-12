package com.sparta.lafesta.common.s3.repository;

import com.sparta.lafesta.common.s3.entity.UserFileOnS3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFileOnS3, Long> {
}
