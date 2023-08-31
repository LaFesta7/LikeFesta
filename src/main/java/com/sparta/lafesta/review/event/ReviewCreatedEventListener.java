package com.sparta.lafesta.review.event;

import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.notification.service.NotificationService;
import com.sparta.lafesta.review.entity.Review;
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
public class ReviewCreatedEventListener implements ApplicationListener<ReviewCreatedEvent> {
    private final NotificationService notificationService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(ReviewCreatedEvent event) {
        Review review = event.getReview();
        String title = "'" + review.getTitle() + "'" + " 게시 안내";
        String editor = review.getUser().getNickname();
        String detail = "팔로우 하신 " + "'" + editor + "'" + "님께서 " + "'" + title + "'"  + "을/를 게시했습니다.";
        LocalDateTime createdAt = review.getCreatedAt();
        List<User> followers = event.getFollowers();
        for (User follower : followers) {
            Notification notification = new Notification(title, detail, createdAt, follower);
            notificationService.saveNotification(notification);
        }
        log.info("리뷰 작성 이벤트 발생");
    }
}
