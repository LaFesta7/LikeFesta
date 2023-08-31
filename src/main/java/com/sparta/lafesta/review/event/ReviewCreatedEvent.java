package com.sparta.lafesta.review.event;

import com.sparta.lafesta.review.entity.Review;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ReviewCreatedEvent extends ApplicationEvent {
    private final Review review;
    private final List<User> followers;

    public ReviewCreatedEvent(Object source, Review review, List<User> followers) {
        super(source);
        this.review = review;
        this.followers = followers;
    }
}
