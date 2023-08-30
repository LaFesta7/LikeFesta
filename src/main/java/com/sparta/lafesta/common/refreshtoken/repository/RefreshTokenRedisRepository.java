package com.sparta.lafesta.common.refreshtoken.repository;

import com.sparta.lafesta.common.refreshtoken.dto.RefreshTokenResponseDto;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenResponseDto, String> {
}
