package com.sharepic.post.exception;

public class IllegalUserForPostUpdateException extends BaseApplicationException {
    public IllegalUserForPostUpdateException(String message) {
        super(message);
    }

    public IllegalUserForPostUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
