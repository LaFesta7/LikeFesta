package com.sparta.lafesta.badge.event;

import com.sparta.lafesta.badge.entity.UserBadge;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserBadgeCreatedEvent extends ApplicationEvent {
    private final UserBadge userBadge;

    public UserBadgeCreatedEvent(Object source, UserBadge userBadge) {
        super(source);
        this.userBadge = userBadge;
    }
}
