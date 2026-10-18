package com.sharepic.user.exception;

public class UserNotFoundException extends BaseApplicationException{
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
