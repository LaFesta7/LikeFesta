package com.sparta.lafesta.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // 알림 메일 보내기
    public void sendNotificationEmail(String toEmail, String mailTitle, String mailContent, String festivalTitle, String festivalDate, String festivalLocate, String htmlTemplate) {
        Context context = new Context(); // 메일 내용
        context.setVariable("mailContent", mailContent); // 페스티벌 리마인드
        context.setVariable("festivalTitle", festivalTitle); // 페스티벌 이름
        context.setVariable("festivalDate", festivalDate); // 페스티벌 일시
        context.setVariable("festivalLocate", festivalLocate); // 페스티벌 장소
        String subject = "[LaFesta] " + mailTitle; // 메일 제목
        String body = templateEngine.process(htmlTemplate, context);
        sendMail(toEmail, subject, body);
    }

    // 메일 전송
    private void sendMail(String toEmail, String subject, String body) {
        MimeMessagePreparator messagePreparator =
                mimeMessage -> {
                    final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setFrom(fromEmail);
                    helper.setTo(toEmail);
                    helper.setSubject(subject);
                    helper.setText(body, true);
                };
        mailSender.send(messagePreparator);
    }
}
