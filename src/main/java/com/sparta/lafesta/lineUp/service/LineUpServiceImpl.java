package com.sparta.lafesta.lineUp.service;

import com.sparta.lafesta.admin.service.AdminService;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.lineUp.dto.LineUpRequestDto;
import com.sparta.lafesta.lineUp.dto.LineUpResponseDto;
import com.sparta.lafesta.lineUp.entity.LineUp;
import com.sparta.lafesta.lineUp.repository.LineUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineUpServiceImpl implements LineUpService {

    private final LineUpRepository lineUpRepository;
    private final FestivalRepository festivalRepository;
    private final AdminService adminService;

    // 라인업 생성
    @Override
    @Transactional
    public LineUpResponseDto saveLineUp(Long festivalId, LineUpRequestDto lineUpRequestDto) throws IOException {
        adminService.checkAdmin();
        LineUp lineUp = lineUpRequestDto.toEntity();
        lineUp.setFestival(festivalRepository.findById(festivalId).orElseThrow(
                () -> new IllegalArgumentException("해당 페스티벌이 존재하지 않습니다.")
        ));
        lineUpRepository.save(lineUp);
        return new LineUpResponseDto(lineUp);
    }

    // 라인업 상세 조회
    @Override
    @Transactional(readOnly = true)
    public LineUpResponseDto findLineUps(Long festivalId, Long lineUpId) throws IOException {
        LineUp lineUp = lineUpRepository.findById(lineUpId).orElseThrow(
                () -> new IllegalArgumentException("해당 라인업이 존재하지 않습니다.")
        );
        return new LineUpResponseDto(lineUp);
    }

    // 라인업 수정
    @Override
    @Transactional
    public LineUpResponseDto updateLineUp(Long festivalId, Long lineUpId, LineUpRequestDto lineUpRequestDto) throws IOException {
        adminService.checkAdmin();
        LineUp lineUp = lineUpRepository.findById(lineUpId).orElseThrow(
                () -> new IllegalArgumentException("해당 라인업이 존재하지 않습니다.")
        );
        lineUp.update(lineUpRequestDto);
        return new LineUpResponseDto(lineUp);
    }

    // 라인업 삭제
    @Override
    @Transactional
    public void deleteLineUp(Long festivalId, Long lineUpId) throws IOException {
        adminService.checkAdmin();
        LineUp lineUp = lineUpRepository.findById(lineUpId).orElseThrow(
                () -> new IllegalArgumentException("해당 라인업이 존재하지 않습니다.")
        );
        lineUpRepository.delete(lineUp);
    }
}
