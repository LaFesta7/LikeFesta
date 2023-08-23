package com.sparta.lafesta.common.s3;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ToString
@Getter
public class TestRequestDto {
    // 카드 이름
    @NotBlank
    private String name;

    // 설명
    private String description;

    // 만기일
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

}
