package com.sharepic.post.exception;

public class PostImageFoundNotException extends BaseApplicationException {
    public PostImageFoundNotException(String message) {
        super(message);
    }

    public PostImageFoundNotException(String message, Throwable cause) {
        super(message, cause);
    }
}
