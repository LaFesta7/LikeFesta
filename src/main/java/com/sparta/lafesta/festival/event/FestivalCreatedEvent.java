package com.sparta.lafesta.festival.event;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class FestivalCreatedEvent extends ApplicationEvent {
    private final Festival festival;
    private final List<User> followers;

    public FestivalCreatedEvent(Object source, Festival festival, List<User> followers) {
        super(source);
        this.festival = festival;
        this.followers = followers;
    }
}
