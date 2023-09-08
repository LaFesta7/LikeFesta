package com.sparta.lafesta.user.repository;

import com.sparta.lafesta.user.entity.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
}