package com.sparta.lafesta.notification.service;

import com.sparta.lafesta.common.exception.NotFoundException;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.email.service.MailService;
import com.sparta.lafesta.festival.service.FestivalService;
import com.sparta.lafesta.notification.dto.NotificationResponseDto;
import com.sparta.lafesta.notification.dto.ReminderDto;
import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.notification.event.ReminderSendEmailEventPublisher;
import com.sparta.lafesta.notification.repository.EmitterRepository;
import com.sparta.lafesta.notification.repository.NotificationRepository;
import com.sparta.lafesta.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final static String NOTIFICATION_NAME = "notification";
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
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
                    String festivalPlace = reminder.getFestivalPlace();
                    String reservationOpenDate = reminder.getReservationOpenDate();
                    String reservationPlace = reminder.getReservationPlace();
                    List<User> festivalFollowUsers = reminder.getFestivalFollowUsers();

                    for (User toUser : festivalFollowUsers) {
                        String toEmail = toUser.getEmail();
                        mailService.sendNotificationEmail(toEmail, mailTitle, mailContent, festivalTitle, festivalOpenDate, festivalPlace, reservationOpenDate, reservationPlace, htmlTemplate);
                    }

                    eventPublisher.publishReminderSendEmailEvent(reminder);
                }
            }
        }
    }

    // 알림 생성 및 실시간 알림 보내기
    @Transactional
    public void saveNotification(Notification notification, Long receiverId) {
        notificationRepository.save(notification);
        sendEmitter(notification, receiverId);
    }

    // 알림 전체 조회
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotifications(User user, Pageable pageable) {
        return notificationRepository.findAllByFollower(user, pageable)
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

    // 실시간 통신 SSE 보내기
    public void sendEmitter(Notification notification, Long receiverId) {
        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
                    try {
                        it.send(SseEmitter.event()
                                .id("")
                                .name(NOTIFICATION_NAME)
                                .data(new NotificationResponseDto(notification)));
                        log.info("SseEmitter send ok");
                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        log.info("SseEmitter send failed");
                        throw new NotFoundException("Connection failed");
                    }
                },
                () -> log.info("No emitter founded")
        );
    }

    // 실시간 통신 SSE 연결
    public SseEmitter connectNotification(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("send");
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(NOTIFICATION_NAME)
                    .data("connect completed"));
        } catch (IOException exception) {
            throw new IllegalArgumentException("connect failed");
        }
        return emitter;
    }

    // id로 알림 찾기
    private Notification findNotification (Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() ->
                new IllegalArgumentException("선택한 알림은 존재하지 않습니다.")
        );
    }
}
