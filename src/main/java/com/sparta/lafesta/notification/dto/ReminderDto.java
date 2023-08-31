package com.sparta.lafesta.notification.dto;

import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.follow.entity.FestivalFollow;
import com.sparta.lafesta.notification.entity.FestivalReminderType;
import com.sparta.lafesta.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
public class ReminderDto {
    private String mailTitle;
    private String mailContent;
    private String festivalTitle;
    private String festivalOpenDate;
    private String festivalLocate;
    private String reservationOpenDate;
    private String reservationPlace;
    private List<User> festivalFollowUsers;
    private List<String> festivalFollowUsersEmail;

    public ReminderDto(Festival festival, FestivalReminderType type) {
        this.mailTitle = findMailtitle(festival, type);
        this.mailContent = findMailContent(festival, type);
        this.festivalTitle = festival.getTitle();
        this.festivalOpenDate = formFestivalDate(festival, FestivalReminderType.FESTIVAL_OPEN);
        this.festivalLocate = festival.getLocation();
        if (type != FestivalReminderType.RESERVATION_OPEN) {
            this.reservationOpenDate = null;
            this.reservationPlace = null;
        } else {
            this.reservationOpenDate = formFestivalDate(festival, FestivalReminderType.RESERVATION_OPEN);
            this.reservationPlace = festival.getReservationPlace();
        }
        this.festivalFollowUsers = festival.getFestivalFollowers().stream()
                .map(FestivalFollow::getFollowingFestivalUser)
                .toList();
        this.festivalFollowUsersEmail = festivalFollowUsers.stream().map(User::getEmail).toList();
    }

    // 타입별로 메일 제목 가져오기
    private String findMailtitle(Festival festival, FestivalReminderType type) {
        String mailTitle = "";
        if (type == FestivalReminderType.FESTIVAL_OPEN) {
            mailTitle = makeFestivalOpenMailTitle(festival);
        } else if (type == FestivalReminderType.RESERVATION_OPEN) {
            mailTitle = makeReservationOpenMailTitle(festival);
        } else if (type == FestivalReminderType.REVIEW_ENCOURAGEMENT) {
            mailTitle = makeReviewEncouragementMailTitle(festival);
        }
        return mailTitle;
    }

    // 타입별로 메일 내용 가져오기
    private String findMailContent(Festival festival, FestivalReminderType type) {
        String mailContent = "";
        if (type == FestivalReminderType.FESTIVAL_OPEN) {
            mailContent = makeFestivalOpenMailContent(festival);
        } else if (type == FestivalReminderType.RESERVATION_OPEN) {
            mailContent = makeReservationOpenMailContent(festival);
        } else if (type == FestivalReminderType.REVIEW_ENCOURAGEMENT) {
            mailContent = makeReviewEncouragementMailContent(festival);
        }
        return mailContent;
    }

    // 페스티벌 오픈 - 메일 제목 만들기
    private String makeFestivalOpenMailTitle(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append(" 개막 ");
        if (getDaysDifference(festival, FestivalReminderType.FESTIVAL_OPEN) > 0) {
            sb.append(getDaysDifference(festival, FestivalReminderType.FESTIVAL_OPEN));
            sb.append("일 전 알림");
        } else {
            sb.append("당일 알림");
        }
        return sb.toString();
    }

    // 페스티벌 오픈 - 메일 내용 만들기
    private String makeFestivalOpenMailContent(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("팔로우 하신 ");
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append(" 개막 ");
        if (getDaysDifference(festival, FestivalReminderType.FESTIVAL_OPEN) > 0) {
            sb.append(getDaysDifference(festival, FestivalReminderType.FESTIVAL_OPEN));
            sb.append("일 전 입니다!");
        } else {
            sb.append("당일입니다!");
        }

        return sb.toString();
    }

    // 예매 오픈 - 메일 제목 만들기
    private String makeReservationOpenMailTitle(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append(" 예매 오픈 ");
        if (getDaysDifference(festival, FestivalReminderType.RESERVATION_OPEN) > 0) {
            sb.append(getDaysDifference(festival, FestivalReminderType.RESERVATION_OPEN));
            sb.append("일 전 알림");
        } else {
            sb.append("당일 알림");
        }
        return sb.toString();
    }

    // 예매 오픈 - 메일 내용 만들기
    private String makeReservationOpenMailContent(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("팔로우 하신 ");
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append(" 예매 오픈 ");
        if (getDaysDifference(festival, FestivalReminderType.RESERVATION_OPEN) > 0) {
            sb.append(getDaysDifference(festival, FestivalReminderType.RESERVATION_OPEN));
            sb.append("일 전 입니다!");
        } else {
            sb.append("당일입니다!");
        }

        return sb.toString();
    }

    // 리뷰 독려 - 메일 제목 만들기
    private String makeReviewEncouragementMailTitle(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append(" 리뷰 작성 알림");

        return sb.toString();
    }

    // 리뷰 독려 - 메일 내용 만들기
    private String makeReviewEncouragementMailContent(Festival festival) {
        StringBuilder sb = new StringBuilder();
        sb.append("팔로우 하신 ");
        sb.append("'");
        sb.append(festival.getTitle());
        sb.append("'");
        sb.append("에 다녀오셨나요? 리뷰를 작성해보세요!");

        return sb.toString();
    }

    // 날짜 차이 구하기
    private long getDaysDifference(Festival festival, FestivalReminderType type) {
        LocalDate today = LocalDate.now();
        LocalDateTime targetDateTime;
        if (type == FestivalReminderType.FESTIVAL_OPEN) {
            targetDateTime = festival.getOpenDate();
        } else {
            targetDateTime = festival.getReservationOpenDate();
        }
        LocalDate targetDate = targetDateTime.toLocalDate();
        return ChronoUnit.DAYS.between(today, targetDate);
    }

    // 페스티벌 날짜 형식 만들기
    private String formFestivalDate(Festival festival, FestivalReminderType type) {
        LocalDateTime date;
        if (type == FestivalReminderType.FESTIVAL_OPEN) {
            date = festival.getOpenDate();
        } else {
            date = festival.getReservationOpenDate();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) HH시 mm분");
        return date.format(formatter);
    }
}
