package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.notification.entity.Notification;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String detail;
    private String timeSinceCreated;
    private Boolean rd;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.detail = notification.getDetail();
        this.timeSinceCreated = Duration.between(notification.getCreatedAt(), LocalDateTime.now()).toString();
        this.rd = notification.getRd();
    }
}
