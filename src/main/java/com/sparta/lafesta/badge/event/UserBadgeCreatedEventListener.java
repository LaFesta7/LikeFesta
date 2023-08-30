package com.sparta.lafesta.badge.event;

import com.sparta.lafesta.badge.entity.UserBadge;
import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.notification.service.NotificationService;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j(topic = "Event Listener")
@Component
@RequiredArgsConstructor
public class UserBadgeCreatedEventListener implements ApplicationListener<UserBadgeCreatedEvent> {
    private final NotificationService notificationService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(UserBadgeCreatedEvent event) {
        UserBadge userBadge = event.getUserBadge();
        String title = userBadge.getBadge().getTitle();
        String detail = title + "뱃지를 획득했습니다.";
        LocalDateTime createdAt = userBadge.getCreatedAt();
        User user = userBadge.getUser();
        Notification notification = new Notification(title, detail, createdAt, user);
        notificationService.saveNotification(notification);
        log.info("유저 뱃지 획득 이벤트 발생");
    }
}
