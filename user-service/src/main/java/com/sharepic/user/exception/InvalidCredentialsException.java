package com.sharepic.user.exception;

public class InvalidCredentialsException extends BaseApplicationException{
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
