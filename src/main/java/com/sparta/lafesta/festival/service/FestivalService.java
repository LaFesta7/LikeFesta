package com.sparta.lafesta.festival.service;

import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface FestivalService {
    /**
     * 페스티벌 생성
     * @param requestDto 생성할 페스티벌의 정보
     * @param user 권한 확인
     * @return 페스티벌 추가 결과
     */
    FestivalResponseDto createFestival(FestivalRequestDto requestDto, User user);

    /**
     * 전체 페스티벌 조회
     * @return 전체 페스티벌 조회 결과
     */
    List<FestivalResponseDto> selectFestivals();

    /**
     * 페스티벌 상세 조회
     * @param festivalId 조회할 페스티벌의 id
     * @param user 권한 확인
     * @return 페스티벌 상세 조회 결과
     */
    FestivalResponseDto selectFestival(Long festivalId, User user);

    /**
     * 페스티벌 수정
     * @param festivalId 수정할 페스티벌의 id
     * @param requestDto 수정할 정보
     * @param user 권한 확인
     * @return 페스티벌 수정 결과
     */
    FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto, User user);

    /**
     * 페스티벌 삭제
     * @param festivalId 삭제할 페스티벌의 id
     * @param user 권한 확인
     */
    void deleteFestival(Long festivalId, User user);

    /**
     * 선택한 페스티벌 좋아요 추가
     * @param festivalId 좋아요 추가할 페스티벌의 id
     * @param user 권한 확인
     * @return 좋아요 추가 결과
     */
    FestivalResponseDto createFestivalLike(Long festivalId, User user);

    /**
     * 선택한 페스티벌 좋아요 취소
     * @param festivalId 좋아요 취소할 페스티벌의 id
     * @param user 권한 확인
     * @return 좋아요 취소 결과
     */
    FestivalResponseDto deleteFestivalLike(Long festivalId, User user);
}
