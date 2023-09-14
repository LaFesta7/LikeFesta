package com.sparta.lafesta.festival.event;

import com.sparta.lafesta.festival.entity.Festival;
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
public class FestivalCreatedEventListener implements ApplicationListener<FestivalCreatedEvent> {
    private final NotificationService notificationService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(FestivalCreatedEvent event) {
        Festival festival = event.getFestival();
        String title = "'" + festival.getTitle() + "' 게시 안내";
        String editor = festival.getUser().getNickname();
        String detail = "팔로우 하신 '" + editor + "'님께서 '" + festival.getTitle() + "' 페스티벌을 게시했습니다.";
        LocalDateTime createdAt = festival.getCreatedAt();
        String destination = "/api/festivals/" + festival.getId() + "/page";
        List<User> followers = event.getFollowers();
        for (User follower : followers) {
            Notification notification = new Notification(title, detail, createdAt, destination, follower);
            notificationService.saveNotification(notification, follower.getId());
        }
        log.info("페스티벌 작성 이벤트 발생");
    }
}
