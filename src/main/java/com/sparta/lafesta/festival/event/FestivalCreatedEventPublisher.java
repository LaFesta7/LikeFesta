package com.sparta.lafesta.festival.event;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.service.FollowService;
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
public class FestivalCreatedEventPublisher {
    public final ApplicationEventPublisher eventPublisher;
    private final FollowService followService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishFestivalCreatedEvent(Festival festival) {
        User editor = festival.getUser();
        List<User> followers = followService.findFollowers(editor);
        FestivalCreatedEvent event = new FestivalCreatedEvent(this, festival, followers);
        eventPublisher.publishEvent(event);
        log.info("페스티벌 작성 이벤트 생성");
    }
}
