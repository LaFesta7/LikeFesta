package com.sparta.lafesta.user.repository;

import com.sparta.lafesta.user.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndConfirm(String email, Boolean confirm);

    Optional<VerificationCode> findByEmailAndCode(String email, String code);

    void deleteByExpirationTimeBefore(LocalDateTime now);
}