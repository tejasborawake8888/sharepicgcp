package com.sharepic.post.exception;

import com.sharepic.post.enums.ErrorCode;
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

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, ErrorCode.POST_NOT_FOUND.getErrorCode(),ErrorCode.POST_NOT_FOUND.getMsg());
    }

    @ExceptionHandler(IllegalUserForPostUpdateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalUserForPostUpdateException(IllegalUserForPostUpdateException ex){
        return build(HttpStatus.FORBIDDEN,ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getErrorCode(), ErrorCode.ILLEGAL_USER_FOR_POST_UPDATE.getMsg());
    }
}
