package com.sparta.lafesta.notification.controller;

import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.notification.dto.NotificationResponseDto;
import com.sparta.lafesta.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "알림 관련 API", description = "알림 관련 API 입니다.")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    @Operation(summary = "알림 전체 조회", description = "로그인 한 유저의 정보를 가져와, 해당 유저에게 온 알림을 모두 조회합니다.")
    public List<NotificationResponseDto> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.getNotifications(userDetails.getUser());
    }

    @PatchMapping("/users/{userId}/notifications/{notificationId}")
    @Operation(summary = "알림 읽음 처리", description = "@PathVariable을 통해 notificationId를 받아와, 해당 알림을 읽음 처리합니다.")
    public ResponseEntity<NotificationResponseDto> readNotification(
            @PathVariable Long notificationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(notificationService.readNotification(notificationId, userDetails.getUser()));
    }
}
