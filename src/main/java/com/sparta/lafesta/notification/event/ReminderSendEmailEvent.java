package com.sparta.lafesta.notification.event;

import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ReminderSendEmailEvent extends ApplicationEvent {
    private final ReminderDto reminder;
    private final List<User> followers;

    public ReminderSendEmailEvent(Object source, ReminderDto reminder, List<User> followers) {
        super(source);
        this.reminder = reminder;
        this.followers = followers;
    }
}
