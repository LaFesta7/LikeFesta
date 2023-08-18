package com.sparta.lafesta.festival.service;

import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;

    // 페스티벌 등록
    @Override
    @Transactional
    public FestivalResponseDto createFestival(FestivalRequestDto requestDto, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Festival festival = new Festival(requestDto);
        festivalRepository.save(festival);
        return new FestivalResponseDto(festival);
    }

    // 페스티벌 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFestivals() {
        return festivalRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(FestivalResponseDto::new).collect(Collectors.toList());
    }

    // 페스티벌 상세 조회
    @Override
    @Transactional(readOnly = true)
    public FestivalResponseDto selectFestival(Long festivalId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        return new FestivalResponseDto(findFestival(festivalId));
    }

    // 페스티벌 내용 수정
    @Override
    @Transactional
    public FestivalResponseDto modifyFestival(Long festivalId, FestivalRequestDto requestDto, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Festival festival = findFestival(festivalId);
        festival.modify(requestDto);
        return new FestivalResponseDto(festival);
    }

    // 페스티벌 삭제
    @Override
    @Transactional
    public void deleteFestival(Long festivalId, User user) {
        // user 권한 확인 예외처리 추후 추가 작성 예정
        Festival festival = findFestival(festivalId);
        festivalRepository.delete(festival);
    }

    // 페스티벌 id로 페스티벌 찾기
    public Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌은 존재하지 않습니다.")
        );
    }
}
