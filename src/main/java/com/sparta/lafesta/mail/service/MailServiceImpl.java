package com.sparta.lafesta.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;


@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender emailSender;

    private String ePw; //인증번호

    //메일 내용 작성
    @Override
    public MimeMessage createMessage(String receiver)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, receiver); //받는 사람
        message.setSubject("LaFesta 이메일 인증 코드입니다."); //제목
        message.setText("이메일 인증 코드: " + ePw);
        message.setFrom(new InternetAddress("LaFesta@naver.com", "Lafesta_Admin")); //보내는 사람(임시)

        return message;
    }

    //랜덤 인증 코드 생성 - 추후 수정할 것
    @Override
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

    //메일 발송
    @Override
    public String sendMessage(String receiver) throws Exception {

        MimeMessage message = createMessage(receiver);
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }
}
