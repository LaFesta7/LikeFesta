package com.sparta.lafesta.review.event;

import com.sparta.lafesta.follow.service.FollowService;
import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j(topic = "Event Publisher")
@Component
@EnableAsync
@RequiredArgsConstructor
public class ReviewCreatedEventPublisher {
    public final ApplicationEventPublisher eventPublisher;
    private final FollowService followService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishReviewCreatedEvent(Review review) {
        User editor = review.getUser();
        List<User> followers = followService.findFollowers(editor);
        ReviewCreatedEvent event = new ReviewCreatedEvent(this, review, followers);
        eventPublisher.publishEvent(event);
        log.info("리뷰 작성 이벤트 생성");
    }
}
