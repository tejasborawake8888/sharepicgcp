package com.sharepic.user.exception;

public class PasswordNotMatchedException extends BaseApplicationException{
    public PasswordNotMatchedException(String message) {
        super(message);
    }

    public PasswordNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }
}
