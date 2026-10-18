package com.sharepic.feed.exception;

import com.sharepic.feed.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponse> build(
            HttpStatus statusCode,
            int errorCode,
            String message
    ){
        ErrorResponse response = new ErrorResponse(
                message, Instant.now(),errorCode
        );
        return ResponseEntity.status(statusCode).body(response);
    }

}
