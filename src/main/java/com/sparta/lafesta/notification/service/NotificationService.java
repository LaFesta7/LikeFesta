package com.sparta.lafesta.notification.service;

import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.notification.dto.ReminderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MailService mailService;
    private final FestivalServiceImpl festivalService;

    // 매일 오전 9시마다 리마인더 알림 메일 발송
    @Scheduled(cron = "0 0 9 * * *")
    public void sendFestivalReminder() {
        // 페스티벌 오픈 당일, 1일 전, 7일 전 리마인더
        List<ReminderDto> festivalOpenReminders = festivalService.getFestivalOpenReminders();
        formNotificationMail(festivalOpenReminders);

        // 페스티벌 예매 오픈 당일, 1일 전 리마인더
        List<ReminderDto> reservationOpenReminders = festivalService.getReservationOpenReminders();
        formNotificationMail(reservationOpenReminders);

        // 페스티벌 1일 후 리뷰 독려 리마인더
        List<ReminderDto> reviewEncouragementReminders = festivalService.getReviewEncouragementReminders();
        formNotificationMail(reviewEncouragementReminders);
    }

    // 알림 메일 서식 담아 메일 보내기
    public void formNotificationMail(List<ReminderDto> reminders) {
        String htmlTemplate = "notification-email.html";
        if (reminders.size() != 0) {
            for (ReminderDto reminder : reminders) {
                if (reminder.getFestivalFollowUsersEmail().size() != 0) {
                    String mailTitle = reminder.getMailTitle();
                    String mailContent = reminder.getMailContent();
                    String festivalTitle = reminder.getFestivalTitle();
                    String festivalOpenDate = reminder.getFestivalOpenDate();
                    String festivalLocate = reminder.getFestivalLocate();
                    String reservationOpenDate = reminder.getReservationOpenDate();
                    String reservationPlace = reminder.getReservationPlace();
                    List<String> festivalFollowUsersEmail = reminder.getFestivalFollowUsersEmail();

                    for (String toEmail : festivalFollowUsersEmail) {
                        mailService.sendNotificationEmail(toEmail, mailTitle, mailContent, festivalTitle, festivalOpenDate, festivalLocate, reservationOpenDate, reservationPlace, htmlTemplate);
                    }
                }
            }
        }
    }
}
