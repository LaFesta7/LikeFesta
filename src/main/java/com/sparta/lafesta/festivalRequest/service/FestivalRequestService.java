package com.sparta.lafesta.festivalRequest.service;

import com.sparta.lafesta.festivalRequest.dto.FestivaRequestlResponseDto;
import com.sparta.lafesta.festivalRequest.dto.FestivalRequestRequestDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface FestivalRequestService {
    /**
     * 페스티벌 생성
     * @param requestDto 생성할 페스티벌 게시 요청글의 정보
     * @param user 권한 확인
     * @return 페스티벌 게시 요청 추가 결과
     */
    FestivaRequestlResponseDto createFestivalRequest(FestivalRequestRequestDto requestDto, User user);

    /**
     * 전체 페스티벌 게시 요청글 조회
     * @return 전체 페스티벌 게시 요청글 조회 결과
     */
    List<FestivaRequestlResponseDto> selectFestivalRequests();

    /**
     * 페스티벌 게시 요청글 상세 조회
     * @param festivalRequestId 조회할 페스티벌 게시 요청글의 id
     * @param user 권한 확인
     * @return 페스티벌 게시 요청글 상세 조회 결과
     */
    FestivaRequestlResponseDto selectFestivalRequest(Long festivalRequestId, User user);

    /**
     * 페스티벌 게시 요청글 수정
     * @param festivalRequestId 수정할 페스티벌 게시 요청글의 id
     * @param requestDto 수정할 정보
     * @param user 권한 확인
     * @return 페스티벌 게시 요청글 수정 결과
     */
    FestivaRequestlResponseDto modifyFestivalRequest(Long festivalRequestId, FestivalRequestRequestDto requestDto, User user);

    /**
     * 페스티벌 게시 요청글 삭제
     * @param festivalRequestId 삭제할 페스티벌 게시 요청글의 id
     * @param user 권한 확인
     */
    void deleteFestivalRequest(Long festivalRequestId, User user);
}
