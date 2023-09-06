package com.sparta.lafesta.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawalRequestDto {

    @NotBlank
    private String password;
}
