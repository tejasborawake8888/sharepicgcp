package com.sharepic.user.exception;

import com.sharepic.user.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidException(MethodArgumentNotValidException ex){
        log.warn("validation failed with below exception: ", ex);
        return build(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED.getErrorCode(),ErrorCode.VALIDATION_FAILED.getMsg());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex) {

        log.warn("Access denied: {}", ex.getMessage());

        return build(
                HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED.getErrorCode(),
                ErrorCode.ACCESS_DENIED.getMsg()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
        log.error("Internal Server error ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.INTERNAL_ERROR.getErrorCode(), ErrorCode.INTERNAL_ERROR.getMsg());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return build(HttpStatus.CONFLICT,ErrorCode.USER_ALREADY_EXISTS.getErrorCode(), ErrorCode.USER_ALREADY_EXISTS.getMsg());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(InvalidCredentialsException ex){
        return build(HttpStatus.UNAUTHORIZED,ErrorCode.INVALID_CREDENTIALS.getErrorCode(), ErrorCode.INVALID_CREDENTIALS.getMsg());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        return build(HttpStatus.UNAUTHORIZED,ErrorCode.USER_NOT_FOUND.getErrorCode(), ErrorCode.USER_NOT_FOUND.getMsg());
    }

}
