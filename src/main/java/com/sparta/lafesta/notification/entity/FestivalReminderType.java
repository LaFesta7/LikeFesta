package com.sparta.lafesta.notification.entity;

public enum FestivalReminderType {
    FESTIVAL_OPEN("festivalOpen"),
    RESERVATION_OPEN("reservationOpen"),
    REVIEW_ENCOURAGEMENT("reviewEncouragement");

    private final String type;

    FestivalReminderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

