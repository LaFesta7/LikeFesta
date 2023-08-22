package com.sparta.lafesta.admin.service;

import com.sparta.lafesta.admin.dto.OrganizerResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;

public interface AdminService {
    /**
     * 주최사 가입 인증 요청 목록 조회
     * @param user 권한 확인
     * @return 주최사 가입 인증 요청 목록
     */
    List<OrganizerResponseDto> selectOrganizerRequests(User user);

    /**
     * 주최사 가입 인증 승인
     * @param userId 댓글을 생성할 리뷰의 id
     * @param user 권한 확인
     * @return 주최사 가입 인증 허가 처리 결과
     */
    OrganizerResponseDto modifyUserRoleOrganizer(Long userId, User user);
}
