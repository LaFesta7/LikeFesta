package com.sparta.lafesta.mail.service;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {

  MimeMessage createMessage(String receiver)
      throws MessagingException, UnsupportedEncodingException;

  String createKey();

  String sendMessage(String receiver) throws Exception;
}
