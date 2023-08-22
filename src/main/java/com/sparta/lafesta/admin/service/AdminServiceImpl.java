package com.sparta.lafesta.admin.service;

import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.admin.dto.OrganizerResponseDto;
import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import com.sparta.lafesta.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    // 주최사 가입 인증 요청 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrganizerResponseDto> selectOrganizerRequests(User user) {
        // admin 권한 확인
        checkAdminRole(user);

        return userRepository.findAllByOrganizerRequest(Boolean.TRUE)
                .stream().map(OrganizerResponseDto::new).toList();
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
        // 주최사 이미 인증이 되어 있는지 확인
        if (organizer.getRole().equals(UserRoleEnum.ORGANIZER)) {
            throw new IllegalArgumentException("해당 유저의 주최사 권한이 이미 존재합니다.");
        }

        organizer.approveOrganizer();

        return new OrganizerResponseDto(organizer);
    }

    // 유저 삭제
    @Override
    @Transactional
    public void deleteUser(Long userId, User user) {
        // admin 권한 확인
        checkAdminRole(user);

        userRepository.delete(findUser(userId));
    }

    // admin 권한 확인
    private void checkAdminRole(User user) {
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
}
