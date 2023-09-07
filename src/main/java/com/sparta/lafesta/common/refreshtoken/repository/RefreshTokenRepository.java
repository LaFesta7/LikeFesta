package com.sparta.lafesta.common.refreshtoken.repository;

import com.sparta.lafesta.common.refreshtoken.entity.UserToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<UserToken, String> {
    Optional<UserToken> findByAccessToken(String accessToken);
}
