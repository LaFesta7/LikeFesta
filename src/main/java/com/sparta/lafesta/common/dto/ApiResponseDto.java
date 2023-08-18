package com.sparta.lafesta.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseDto {
    private int statusCode;
    private String statusMessage;
}
