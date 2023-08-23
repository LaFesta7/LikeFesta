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

    public void sendNotification(String toEmail, String title, String content) {
        Context context = new Context();
        context.setVariable("content", content);
        String subject = "[LaFesta] " + title; // 메일 제목
        String body = templateEngine.process("notification-email.html", context);
        sendMail(toEmail, subject, body);
    }

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
