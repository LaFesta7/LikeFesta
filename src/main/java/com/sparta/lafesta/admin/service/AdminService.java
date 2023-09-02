package com.sparta.lafesta.admin.service;

import com.sparta.lafesta.admin.dto.OrganizerResponseDto;
import com.sparta.lafesta.festivalRequest.dto.FestivaRequestlResponseDto;
import com.sparta.lafesta.user.entity.User;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    /**
     * 주최사 가입 인증 요청 목록 조회
     * @param user 권한 확인
     * @param pageable 페이징 정보
     * @return 주최사 가입 인증 요청 목록
     */
    List<OrganizerResponseDto> selectOrganizerRequests(User user, Pageable pageable);

    /**
     * 주최사 가입 인증 승인
     * @param userId 주최사 가입 인증을 승인할 유저의 id
     * @param user 권한 확인
     * @return 주최사 가입 인증 허가 처리 결과
     */
    OrganizerResponseDto modifyUserRoleOrganizer(Long userId, User user);

    /**
     * 유저 삭제
     * @param userId 삭제할 유저의 id
     * @param user 권한 확인
     * @return 유저 삭제 처리결과
     */
    void deleteUser(Long userId, User user);

    /**
     * 페스티벌 게시 요청 미승인 목록 조회
     * @param user 권한 확인
     * @param pageable 페이징 정보
     * @return 페스티벌 게시 요청 미승인 목록
     */
    List<FestivaRequestlResponseDto> selectFestivalRequestNotAdminApproval(User user, Pageable pageable);

    /**
     * 페스티벌 게시 요청 승인
     * @param festivalRequestId 승인할 페스티벌 게시 요청의 id
     * @param user 권한 확인
     * @return 페스티벌 게시 요청 승인 처리 결과
     */
    FestivaRequestlResponseDto modifyFestivalRequestAdminApproval(Long festivalRequestId, User user);

    /**
     * admin 권한 확인
     * @param user 권한 확인
     */
    void checkAdminRole(User user);
}
