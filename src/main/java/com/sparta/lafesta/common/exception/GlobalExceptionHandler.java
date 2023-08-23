package com.sparta.lafesta.common.exception;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex) {
        ApiResponseDto apiException = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(

                apiException,

                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }
}
