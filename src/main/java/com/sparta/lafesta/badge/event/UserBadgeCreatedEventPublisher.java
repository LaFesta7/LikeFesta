package com.sparta.lafesta.badge.event;

import com.sparta.lafesta.badge.entity.UserBadge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Event Publisher")
@Component
@EnableAsync
@RequiredArgsConstructor
public class UserBadgeCreatedEventPublisher {
    public final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishUserBadgeCreatedEvent(UserBadge userBadge) {
        UserBadgeCreatedEvent event = new UserBadgeCreatedEvent(this, userBadge);
        eventPublisher.publishEvent(event);
        log.info("유저 뱃지 획득 이벤트 생성");
    }
}
