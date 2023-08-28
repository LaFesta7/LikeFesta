package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.notification.entity.Notification;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {
    private Long id;
    private String editor;
    private String title;
    private String detail;
    private String timeSinceCreated;
    private Boolean read;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle() + " 게시 안내";
        this.editor = notification.getEditor();
        this.detail = "팔로우 하신 " + getEditor() + "님께서 " + getTitle() + "을/를 게시했습니다.";
        this.timeSinceCreated = Duration.between(notification.getCreatedAt(), LocalDateTime.now()).toString();
        this.read = notification.getRead();
    }
}
