package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.common.entity.DurationFormatter;
import com.sparta.lafesta.notification.entity.Notification;
import lombok.Getter;

@Getter
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String detail;
    private String timeSinceCreated;
    private String destination;
    private Boolean rd;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.detail = notification.getDetail();
        this.timeSinceCreated = DurationFormatter.format(notification.getCreatedAt());
        this.destination = notification.getDestination();
        this.rd = notification.getRd();
    }
}
