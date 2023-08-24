package com.sparta.lafesta.festivalRequest.service;

import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.festivalRequest.dto.FestivaRequestlResponseDto;
import com.sparta.lafesta.festivalRequest.dto.FestivalRequestRequestDto;
import com.sparta.lafesta.festivalRequest.entity.FestivalRequest;
import com.sparta.lafesta.festivalRequest.repository.FestivalRequestRepository;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalRequestServiceImpl implements FestivalRequestService {
    private final FestivalRequestRepository festivalRequestRepository;

    // 페스티벌 게시 요청글 등록
    @Override
    @Transactional
    public FestivaRequestlResponseDto createFestivalRequest(FestivalRequestRequestDto requestDto, User user) {
        // 허가된 주최사, 관리자 접근 시 예외처리
        if (!user.getRole().getAuthority().equals("ROLE_USER")) {
            throw new UnauthorizedException("해당 요청에 접근할 수 없습니다.");
        }
        FestivalRequest festivalRequest = new FestivalRequest(requestDto, user);
        festivalRequestRepository.save(festivalRequest);
        return new FestivaRequestlResponseDto(festivalRequest);
    }

    // 페스티벌 게시 요청글 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivaRequestlResponseDto> selectFestivalRequests() {
        return festivalRequestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(FestivaRequestlResponseDto::new).collect(Collectors.toList());
    }

    // 페스티벌 게시 요청글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public FestivaRequestlResponseDto selectFestivalRequest(Long festivalRequestId, User user) {
        return new FestivaRequestlResponseDto(findFestivalRequest(festivalRequestId));
    }

    // 페스티벌 게시 요청글 내용 수정
    @Override
    @Transactional
    public FestivaRequestlResponseDto modifyFestivalRequest(Long festivalRequestId, FestivalRequestRequestDto requestDto, User user) {
        FestivalRequest festivalRequest = findFestivalRequest(festivalRequestId);

        // 본인이 작성한 글만 수정 가능
        if (!festivalRequest.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        festivalRequest.modify(requestDto);
        return new FestivaRequestlResponseDto(festivalRequest);
    }

    // 페스티벌 게시 요청글 삭제
    @Override
    @Transactional
    public void deleteFestivalRequest(Long festivalRequestId, User user) {
        FestivalRequest festivalRequest = findFestivalRequest(festivalRequestId);

        // 일반 사용자의 경우 본인이 작성한 글만, 관리자는 모든 글에 대하여 삭제 가능
        if (!festivalRequest.getUser().getId().equals(user.getId())
                && !user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("해당 게시글을 삭제할 수 있는 권한이 없습니다.");
        }

        festivalRequestRepository.delete(festivalRequest);
    }

    // 페스티벌 게시 요청글 id로 페스티벌 게시 요청글 찾기
    public FestivalRequest findFestivalRequest(Long festivalRequestId) {
        return festivalRequestRepository.findById(festivalRequestId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌 게시 요청글은 존재하지 않습니다.")
        );
    }
}
