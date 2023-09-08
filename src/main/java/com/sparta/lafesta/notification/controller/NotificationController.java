package com.sparta.lafesta.notification.controller;

import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.notification.dto.NotificationResponseDto;
import com.sparta.lafesta.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "알림 관련 API", description = "알림 관련 API 입니다.")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/notifications")
    @Operation(summary = "알림 전체 조회", description = "로그인 한 유저의 정보를 가져와, 해당 유저에게 온 알림을 모두 조회합니다.")
    public List<NotificationResponseDto> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault(size=10, sort="createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        return notificationService.getNotifications(userDetails.getUser(), pageable);
    }

    @PatchMapping("/users/notifications/{notificationId}")
    @Operation(summary = "알림 읽음 처리", description = "@PathVariable을 통해 notificationId를 받아와, 해당 알림을 읽음 처리합니다.")
    public ResponseEntity<NotificationResponseDto> readNotification(
            @PathVariable Long notificationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(notificationService.readNotification(notificationId, userDetails.getUser()));
    }

    // 알림 실시간 통신 SSE 적용
    @GetMapping(value = "/users/notifications/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("subscribe");
        return notificationService.connectNotification(userDetails.getUser().getId());
    }

}
