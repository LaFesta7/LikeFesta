package com.sparta.lafesta.festival.service;

import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;

import java.util.List;

public interface FestivalService {
    /**
     * @param requestDto
     * @param user
     * @return 페스티벌 추가 결과
     */
    FestivalResponseDto createFestival(FestivalRequestDto requestDto, User user);

    /**
     * @return 전체 페스티벌 조회 결과
     */
    List<FestivalResponseDto> selectFestivals();

    /**
     * @param festivalId
     * @param user
     * @return 페스티벌 상세 조회 결과
     */
    FestivalResponseDto selectFestival(Long festivalId, User user);

    /**
     * @param festivalId
     * @param requestDto
     * @param user
     * @return
     */
    FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto, User user);

    /**
     * @param festivalId
     * @param user
     */
    void deleteFestival(Long festivalId, User user);
}
