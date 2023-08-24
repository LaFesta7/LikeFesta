package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FestivalReminderResponseDto {
    private String mailTitle;
    private String mailContent;
    private String festivalTitle;
    private String festivalDate;
    private String festivalLocate;
    private List<String> festivalFollowUsersEmail;

    public FestivalReminderResponseDto(Festival festival) {
        this.mailTitle = makeNotificationMailTitle(festival);
        this.mailContent = makeNotificationMailContent(festival);
        this.festivalTitle = festival.getTitle();
        this.festivalDate = formFestivalDate(festival);
        this.festivalLocate = festival.getLocation();
        this.festivalFollowUsersEmail = festival.getFestivalFollowers().stream()
                .map(FestivalFollow::getFollowingFestivalUser)
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    // 메일 제목 만들기
    private String makeNotificationMailTitle(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("' ");
        sb.append(festival.getTitle());
        sb.append(" '");
        sb.append(" 개막 ");
        if (getDaysDifference(festival) > 0) {
            sb.append(getDaysDifference(festival));
            sb.append("일 전 알림");
        } else {
            sb.append("당일 알림");
        }
        return sb.toString();
    }

    // 메일 내용 만들기
    private String makeNotificationMailContent(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("팔로우 하신 페스티벌");
        sb.append(" 개막 ");
        if (getDaysDifference(festival) > 0) {
            sb.append(getDaysDifference(festival));
            sb.append("일 전 입니다!");
        } else {
            sb.append("당일입니다!");
        }

        return sb.toString();
    }

    // 날짜 차이 구하기
    private long getDaysDifference(Festival festival) {
        LocalDate today = LocalDate.now();
        LocalDateTime festivalDateTime = festival.getOpenDate();
        LocalDate festivalDate = festivalDateTime.toLocalDate();
        return ChronoUnit.DAYS.between(today, festivalDate);
    }

    // 페스티벌 날짜 형식 만들기
    private String formFestivalDate(Festival festival) {
        LocalDateTime date = festival.getOpenDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) HH시 mm분");
        return date.format(formatter);
    }
}
