package com.sparta.lafesta.notification.service;

import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.notification.dto.FestivalReminderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MailService mailService;
    private final FestivalServiceImpl festivalService;

    // 페스티벌 당일, 1일 전, 7일 전 리마인더 알림 메일로 발송
    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시마다 실행
    public void sendFestivalReminder() {
        String htmlTemplate = "notification-email.html";
        List<FestivalReminderResponseDto> festivalReminders = festivalService.getFestivalReminders();
        if (festivalReminders.size() != 0) {
            for (FestivalReminderResponseDto festivalReminder : festivalReminders) {
                if (festivalReminder.getFestivalLikeUsersEmail().size() != 0) {
                    String mailTitle = festivalReminder.getMailTitle();
                    String mailContent = festivalReminder.getMailContent();
                    String festivalTitle = festivalReminder.getFestivalTitle();
                    String festivalDate = festivalReminder.getFestivalDate();
                    String festivalLocate = festivalReminder.getFestivalLocate();
                    List<String> festivalLikeUsersEmail = festivalReminder.getFestivalLikeUsersEmail();

                    for (String toEmail : festivalLikeUsersEmail) {
                        mailService.sendNotificationEmail(toEmail, mailTitle, mailContent, festivalTitle, festivalDate, festivalLocate, htmlTemplate);
                    }
                }
            }
        }
    }
}
