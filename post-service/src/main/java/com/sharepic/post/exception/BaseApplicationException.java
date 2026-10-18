package com.sharepic.post.exception;

public class BaseApplicationException extends RuntimeException{
    protected BaseApplicationException(String message) {
        super(message);
    }

    protected BaseApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
