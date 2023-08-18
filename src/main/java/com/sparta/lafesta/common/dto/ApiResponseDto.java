package com.sparta.lafesta.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String msg;
    private Integer statusCode;

    public ApiResponseDto(String msg, Integer statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}