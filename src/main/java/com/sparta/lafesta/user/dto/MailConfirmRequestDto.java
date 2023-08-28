package com.sparta.lafesta.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MailConfirmRequestDto {

    @NotBlank
    @Email
    private String email;
}