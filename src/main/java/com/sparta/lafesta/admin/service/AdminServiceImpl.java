package com.sparta.lafesta.admin.service;

import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.admin.dto.OrganizerResponseDto;
import com.sparta.lafesta.festivalRequest.dto.FestivaRequestlResponseDto;
import com.sparta.lafesta.festivalRequest.entity.FestivalRequest;
import com.sparta.lafesta.festivalRequest.repository.FestivalRequestRepository;
import com.sparta.lafesta.user.dto.UserInfoResponseDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final FestivalRequestRepository festivalRequestRepository;

    // 주최사 가입 인증 요청 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizerResponseDto> selectOrganizerRequests(User user, Pageable pageable) {
        // admin 권한 확인
        checkAdminRole(user);

        return userRepository.findAllByOrganizerRequest(Boolean.TRUE, pageable)
                .map(OrganizerResponseDto::new);
    }

    // 주최사 가입 인증 승인
    @Override
    @Transactional
    public OrganizerResponseDto modifyUserRoleOrganizer(Long userId, User user) {
        // admin 권한 확인
        checkAdminRole(user);

        User organizer = findUser(userId);
        // 주최사를 요청하지 않은 다른 id를 입력한 경우
        if (!organizer.getOrganizerRequest()) {
            throw new IllegalArgumentException("해당 유저는 주최사 권한을 요청하지 않았습니다.");
        }

        organizer.approveOrganizer();

        return new OrganizerResponseDto(organizer);
    }

    // 페스티벌 게시 요청 미승인 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<FestivaRequestlResponseDto> selectFestivalRequestNotAdminApproval(User user, Pageable pageable) {
        // admin 권한 확인
        checkAdminRole(user);

        return festivalRequestRepository.findAllByAdminApproval(Boolean.FALSE, pageable)
                .map(FestivaRequestlResponseDto::new);
    }

    // 페스티벌 게시 요청 승인
    @Override
    @Transactional
    public FestivaRequestlResponseDto modifyFestivalRequestAdminApproval(Long festivalRequestId, User user) {
        // admin 권한 확인
        checkAdminRole(user);

        FestivalRequest festivalRequest = findFestivalRequest(festivalRequestId);
        // 이미 승인이 되어 있는지 확인
        if (festivalRequest.getAdminApproval()) {
            throw new IllegalArgumentException("해당 게시 요청글은 이미 승인 되었습니다.");
        }

        festivalRequest.approveFestivalRequest();

        return new FestivaRequestlResponseDto(festivalRequest);
    }

    // 전체 유저 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<UserInfoResponseDto> selectUsers(User user, Pageable pageable) {
        // admin 권한 확인
        checkAdminRole(user);

        return userRepository.findAll(pageable).map(UserInfoResponseDto::new);
    }

    // 유저 삭제
    @Override
    @Transactional
    public void deleteUser(Long userId, User user) {
        // admin 권한 확인
        checkAdminRole(user);
        if (findUser(userId).getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자는 삭제할 수 없습니다.");
        }
        userRepository.delete(findUser(userId));
    }

    // admin 권한 확인
    @Override
    public void checkAdminRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new UnauthorizedException("해당 페이지에 접근할 권한이 없습니다.");
        }
    }

    // 사용자 id로 사용자 찾기
    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("선택한 유저는 존재하지 않습니다.")
        );
    }

    // 페스티벌 게시 요청글 id로 페스티벌 게시 요청글 찾기
    private FestivalRequest findFestivalRequest(Long festivalRequestId) {
        return festivalRequestRepository.findById(festivalRequestId).orElseThrow(() ->
                new NotFoundException("선택한 페스티벌 게시 요청글은 존재하지 않습니다.")
        );
    }
}
