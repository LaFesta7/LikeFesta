package com.sparta.lafesta.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

  private String ePw; //인증번호

  public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  // 메일 형식 만들어 보내기
  public void sendCustomEmail(String toEmail, String title, String content, String htmlTemplate) {
    Context context = new Context();
    context.setVariable("content", content); // 메일 내용
    String subject = "[LaFesta] " + title; // 메일 제목
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

  //인증 메일 내용 작성
  public MimeMessage createMessage(String receiver)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();

    message.addRecipients(MimeMessage.RecipientType.TO, receiver); //받는 사람
    message.setSubject("LaFesta 이메일 인증 코드입니다."); //제목

    Context context = new Context();
    context.setVariable("ePw", ePw);

    String msg = "authentication-email.html";

    String body = templateEngine.process(msg, context);

    message.setText(body, "utf-8", "html");//내용
    message.setFrom(new InternetAddress(fromEmail, "LaFesta_Admin")); //보내는 사람

    return message;
  }

  //랜덤 인증 코드 생성
  public String createKey() {
    StringBuffer key = new StringBuffer();
    Random random = new Random();

    for (int i = 0; i < 8; i++) { // 인증코드 8자리
      int index = random.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

      switch (index) {
        case 0:
          key.append((char) ((int) (random.nextInt(26)) + 97));
          // a~z (ex. 1+97=98 => (char)98 = 'b')
          break;
        case 1:
          key.append((char) ((int) (random.nextInt(26)) + 65));
          // A~Z
          break;
        case 2:
          key.append((random.nextInt(10)));
          // 0~9
          break;
      }
    }
    return key.toString();
  }

  //인증 메일 발송
  public String sendMessage(String receiver) throws Exception {

    ePw = createKey();

    MimeMessage message = createMessage(receiver);
    try {
      mailSender.send(message);
    } catch (MailException es) {
      es.printStackTrace();
      throw new IllegalArgumentException();
    }
    return ePw;
  }
}
