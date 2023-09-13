package com.sparta.lafesta.common.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class DurationFormatter {
    public static String format(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "방금 전";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "분 전";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "시간 전";
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + "일 전";
        } else {
            long weeks = seconds / 604800;
            return weeks + "주 전";
        }
    }
}
