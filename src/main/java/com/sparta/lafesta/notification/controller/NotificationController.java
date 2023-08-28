package com.sparta.lafesta.notification.controller;

import com.sparta.lafesta.common.security.UserDetailsImpl;
import com.sparta.lafesta.notification.dto.NotificationResponseDto;
import com.sparta.lafesta.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    @Operation(summary = "알림 전체 조회")
    public List<NotificationResponseDto> getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return notificationService.getNotifications(userDetails.getUser());
    }

    @PatchMapping("/users/{userId}/notifications/{notificationId}")
    @Operation(summary = "알림 읽음 처리")
    public ResponseEntity<NotificationResponseDto> readNotification(
            @PathVariable Long notificationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(notificationService.readNotification(notificationId, userDetails.getUser()));
    }
}
