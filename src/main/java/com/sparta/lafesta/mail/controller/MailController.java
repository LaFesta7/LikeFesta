package com.sparta.lafesta.mail.controller;

import com.sparta.lafesta.mail.service.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

  private final MailServiceImpl mailServiceImpl;

  @PostMapping("users/login/mail-confirm")
  String mailConfirm(@RequestParam("email") String email) throws Exception {
    String code = mailServiceImpl.sendMessage(email);
    System.out.println("인증코드: " + code);
    return code;
  }

}
