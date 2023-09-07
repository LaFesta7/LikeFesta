package com.sparta.lafesta.lineUp.service;

import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;

import java.io.IOException;
import java.util.List;

public interface LineUpService {

    /**
     * 라인업 상세 조회
     * @param festivalId 조회할 페스티벌
     * @param lineUpId 조회할 라인업
     * @return 라인업 상세 조회 결과
     */
    List<LineUpResponseDto> findLineUps(Long festivalId, Long lineUpId) throws IOException;

    /**
     * 라인업 생성
     * @param festivalId 생성할 페스티벌
     * @param lineUpRequestDto 생성할 라인업 정보
     * @return 라인업 저장 결과
     */
    LineUpResponseDto saveLineUp(Long festivalId, LineUpRequestDto lineUpRequestDto);

    /**
     * 라인업 수정
     * @param festivalId 수정할 페스티벌
     * @param lineUpId 수정할 라인업
     * @param lineUpRequestDto 수정할 라인업 정보
     * @return 라인업 수정 결과
     */
    LineUpResponseDto updateLineUp(Long festivalId, Long lineUpId, LineUpRequestDto lineUpRequestDto);

    /**
     * 라인업 삭제
     * @param festivalId 삭제할 페스티벌
     * @param lineUpId 삭제할 라인업
     * @return 라인업 삭제 결과
     */
    void deleteLineUp(Long festivalId, Long lineUpId);
}
