package com.sparta.lafesta.festival.service;

import com.sparta.lafesta.festival.dto.FestivalRequestDto;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.like.festivalLike.repository.FestivalLikeRepository;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {
    private final FestivalRepository festivalRepository;
    private final FestivalLikeRepository festivalLikeRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

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

    // 페스티벌 좋아요 추가
    @Override
    @Transactional
    public FestivalResponseDto createFestivalLike(Long festivalId, User user) {
        Festival festival = findFestival(festivalId);
        // 좋아요를 이미 누른 경우 오류 반환
        if (findFestivalLike(user, festival) != null) {
            throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 페스티벌에 좋아요 추가
        festivalLikeRepository.save(new FestivalLike(user, festival));

        return new FestivalResponseDto(festival);
    }

    // 페스티벌 좋아요 취소
    @Override
    public FestivalResponseDto deleteFestivalLike(Long festivalId, User user) {
        FestivalResponseDto response = transactionTemplate.execute(status -> {
            Festival festival = findFestival(festivalId);
            // 좋아요를 누르지 않은 경우 오류 반환
            if (findFestivalLike(user, festival) == null) {
                throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
            }
            // 오류가 나지 않을 경우 해당 페스티벌에 좋아요 취소
            festivalLikeRepository.delete(findFestivalLike(user, festival));

            // 여기에서 커밋을 수행 (트랜잭션 내에서 커밋 또는 롤백을 수행할 수 있음)
            status.flush();

            // FestivalResponseDto 생성 후 반환
            return new FestivalResponseDto(festival);
        });

        // 위에서 커밋이 수행되었으므로 FestivalResponseDto에서 새로운 likeCnt를 가져올 수 있음
        return response;
    }

    // 페스티벌 id로 페스티벌 찾기
    public Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌은 존재하지 않습니다.")
        );
    }

    // 페스티벌과 사용자로 좋아요 찾기
    private FestivalLike findFestivalLike(User user, Festival festival) {
        return festivalLikeRepository.findByUserAndFestival(user, festival).orElse(null);
    }
}
