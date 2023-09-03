package com.sparta.lafesta.common.refreshtoken.repository;

import com.sparta.lafesta.common.refreshtoken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
