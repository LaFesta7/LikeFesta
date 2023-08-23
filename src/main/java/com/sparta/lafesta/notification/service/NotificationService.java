package com.sparta.lafesta.notification.service;

import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.notification.dto.FestivalReminderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final MailService mailService;
    private final FestivalRepository festivalRepository;


    @Autowired
    public NotificationService(MailService mailService, FestivalRepository festivalRepository) {
        this.mailService = mailService;
        this.festivalRepository = festivalRepository;
    }

    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시마다 실행
    public void sendFestivalReminder() {
        // 조건을 확인하고 필요하다면 이메일 알림을 보냄
        List<FestivalReminderResponseDto> festivalReminders = getFestivalReminders();
        if (festivalReminders.size() != 0) {
            for (FestivalReminderResponseDto festivalReminder : festivalReminders) {
                if (festivalReminder.getFestivalLikeUsersEmail().size() != 0) {
                    String title = festivalReminder.getTitle();
                    String content = festivalReminder.getContent();
                    List<String> festivalLikeUsersEmail = festivalReminder.getFestivalLikeUsersEmail();

                    for (String toEmail : festivalLikeUsersEmail) {
                        mailService.sendNotification(toEmail, title, content);
                    }
                }
            }
        }
    }

    private List<FestivalReminderResponseDto> getFestivalReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate sevenDaysAfter = today.plusDays(7);

        List<LocalDate> dateRanges = Arrays.asList(today, tomorrow, sevenDaysAfter);

        List<Festival> reminderFestivals = dateRanges.stream()
                .map(date -> {
                    LocalDateTime startOfDay = date.atStartOfDay();
                    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                    return festivalRepository.findAllByOpenDateBetween(startOfDay, endOfDay);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return reminderFestivals.stream()
                .map(FestivalReminderResponseDto::new)
                .toList();
    }
}
