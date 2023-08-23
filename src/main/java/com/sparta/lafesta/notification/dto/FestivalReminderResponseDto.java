package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.like.festivalLike.entity.FestivalLike;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FestivalReminderResponseDto {
    private String title;
    private String content;
    private List<String> festivalLikeUsersEmail;

    public FestivalReminderResponseDto(Festival festival) {
        this.title = makeNotificationTitle(festival);
        this.content = makeNotificationContent(festival);
        this.festivalLikeUsersEmail = festival.getFestivalLikes().stream()
                .map(FestivalLike::getUser)
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    private String makeNotificationTitle(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append(festival.getTitle());
        sb.append(" 개최 ");
        if (getDaysDifference(festival) > 0) {
            sb.append(getDaysDifference(festival));
            sb.append("일 전 알림");
        } else {
            sb.append("당일 알림");
        }
        return sb.toString();
    }

    private String makeNotificationContent(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append(festival.getTitle());
        sb.append(" 개최 ");
        if (getDaysDifference(festival) > 0) {
            sb.append(getDaysDifference(festival));
            sb.append("일 전 입니다!");
        } else {
            sb.append("당일입니다!");
        }

        return sb.toString();
    }

    private long getDaysDifference(Festival festival) {
        LocalDate today = LocalDate.now();
        LocalDateTime festivalDateTime = festival.getOpenDate();
        LocalDate festivalDate = festivalDateTime.toLocalDate();
        return ChronoUnit.DAYS.between(today, festivalDate);
    }
}
