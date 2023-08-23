package com.sparta.lafesta.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface MailService {

  MimeMessage createMessage(String receiver)
      throws MessagingException, UnsupportedEncodingException;

  String createKey();

  String sendMessage(String receiver) throws Exception;
}
