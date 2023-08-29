package com.sparta.lafesta.notification.service;

import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.festival.service.FestivalServiceImpl;
import com.sparta.lafesta.notification.dto.NotificationResponseDto;
import com.sparta.lafesta.festival.service.FestivalService;
import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.notification.event.ReminderSendEmailEventPublisher;
import com.sparta.lafesta.notification.repository.NotificationRepository;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MailService mailService;
    private final FestivalService festivalService;

    // 알림
    private final ReminderSendEmailEventPublisher eventPublisher;

    // 매일 오전 9시마다 리마인더 알림 메일 발송
    @Scheduled(cron = "0 0 9 * * *")
    public void sendFestivalReminder() {
        // 페스티벌 오픈 당일, 1일 전, 7일 전 리마인더
        List<ReminderDto> festivalOpenReminders = festivalService.getFestivalOpenReminders();
        sendReminderNotification(festivalOpenReminders);

        // 페스티벌 예매 오픈 당일, 1일 전 리마인더
        List<ReminderDto> reservationOpenReminders = festivalService.getReservationOpenReminders();
        sendReminderNotification(reservationOpenReminders);

        // 페스티벌 1일 후 리뷰 독려 리마인더
        List<ReminderDto> reviewEncouragementReminders = festivalService.getReviewEncouragementReminders();
        sendReminderNotification(reviewEncouragementReminders);
    }

    // 알림 메일 서식 담아 메일 보내기 + 웹 알림 보내기
    public void sendReminderNotification(List<ReminderDto> reminders) {
        String htmlTemplate = "notification-email.html";
        if (reminders.size() > 0) {
            for (ReminderDto reminder : reminders) {
                if (reminder.getFestivalFollowUsers().size() != 0) {
                    String mailTitle = reminder.getMailTitle();
                    String mailContent = reminder.getMailContent();
                    String festivalTitle = reminder.getFestivalTitle();
                    String festivalOpenDate = reminder.getFestivalOpenDate();
                    String festivalLocate = reminder.getFestivalLocate();
                    String reservationOpenDate = reminder.getReservationOpenDate();
                    String reservationPlace = reminder.getReservationPlace();
                    List<User> festivalFollowUsers = reminder.getFestivalFollowUsers();

                    for (User toUser : festivalFollowUsers) {
                        String toEmail = toUser.getEmail();
                        mailService.sendNotificationEmail(toEmail, mailTitle, mailContent, festivalTitle, festivalOpenDate, festivalLocate, reservationOpenDate, reservationPlace, htmlTemplate);
                    }

                    eventPublisher.publishReminderSendEmailEvent(reminder);
                }
            }
        }
    }

    // 알림 생성
    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    // 알림 전체 조회
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotifications(User user) {
        return notificationRepository.findAllByFollower(user)
                .stream().map(NotificationResponseDto::new).toList();
    }

    // 알림 읽음 처리
    @Transactional
    public NotificationResponseDto readNotification(Long notificationId, User user) {
        Notification notification = findNotification(notificationId);
        if (!notification.getFollower().getId().equals(user.getId())) {
            throw new UnauthorizedException("해당 알림을 읽을 권한이 없습니다.");
        }
        if (notification.getRd()) {
            throw new IllegalArgumentException("해당 알림은 이미 읽으셨습니다.");
        }
        notification.readNotification();
        return new NotificationResponseDto(notification);
    }

    // 알림 DB 30분마다 삭제하기
    @Transactional
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void cleanupExpiredNotifications() {
        notificationRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    // id로 알림 찾기
    private Notification findNotification (Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() ->
                new IllegalArgumentException("선택한 알림은 존재하지 않습니다.")
        );
    }
}
