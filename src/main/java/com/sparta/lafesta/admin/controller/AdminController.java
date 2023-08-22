package com.sparta.lafesta.admin.controller;

import com.sparta.lafesta.admin.dto.OrganizerResponseDto;
import com.sparta.lafesta.admin.service.AdminServiceImpl;
import com.sparta.lafesta.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminServiceImpl adminService;

    @GetMapping("/users/organizer-requests")
    @Operation(summary = "주최사 가입 인증 요청 목록 조회", description = "주최사 가입 인증 요청한 목록을 전체 조회합니다.")
    public ResponseEntity<List<OrganizerResponseDto>> selectOrganizerRequests(
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<OrganizerResponseDto> results = adminService.selectOrganizerRequests(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    @PatchMapping("/users/organizer-requests/{userId}")
    @Operation(summary = "주최사 가입 인증 승인", description = "주최사 가입 인증 요청한 사용자에게 인증을 승인해 주최사 권한을 부여합니다.")
    public ResponseEntity<OrganizerResponseDto> modifyUserRoleOrganizer(
            @Parameter(name = "userId", description = "주최사 인증을 허가할 user의 id", in = ParameterIn.PATH) @PathVariable Long userId,
            @Parameter(description = "권한 확인을 위해 필요한 User 정보")@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrganizerResponseDto result = adminService.modifyUserRoleOrganizer(userId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }
}
