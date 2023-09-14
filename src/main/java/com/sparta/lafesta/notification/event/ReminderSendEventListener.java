package com.sparta.lafesta.notification.event;

import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.notification.service.NotificationService;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "Event Listener")
@Component
@RequiredArgsConstructor
public class ReminderSendEventListener implements ApplicationListener<ReminderSendEmailEvent> {
    private final NotificationService notificationService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(ReminderSendEmailEvent event) {
        ReminderDto reminder = event.getReminder();
        String title = reminder.getMailTitle();
        String detail = reminder.getMailContent();
        LocalDateTime createdAt = LocalDateTime.now();
        String destination = "/api/festivals/" + reminder.getFestivalId() + "/page";
        List<User> followers = event.getFollowers();
        for (User follower : followers) {
            Notification notification = new Notification(title, detail, createdAt, destination, follower);
            notificationService.saveNotification(notification, follower.getId());
        }
        log.info("리마인더 이벤트 발생");
    }
}
