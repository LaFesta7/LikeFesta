package com.sparta.lafesta.notification;

import com.sparta.lafesta.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final MailService mailService;

    @Autowired
    public NotificationService(MailService mailService) {
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 16 * * *") // 매일 4시마다 실행
    public void processConditionAndSendNotification() {
        // 조건을 확인하고 필요하다면 이메일 알림을 보냄
        if (conditionIsMet()) {
            String toEmail = "받을email";
            String title = "메일 제목 설정";
            String content = "알림 내용 넣을 예정";
            mailService.sendNotification(toEmail, title, content);
        }
    }

    private boolean conditionIsMet() {
        // 조건을 확인하는 로직
        return true; // 예시로 항상 조건을 충족했다고 가정
    }
}
