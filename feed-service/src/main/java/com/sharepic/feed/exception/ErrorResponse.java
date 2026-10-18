package com.sharepic.feed.exception;

import java.time.Instant;

public class ErrorResponse {
    private final String message;
    private final Instant timestamp;
    private final int errorCode;

    public ErrorResponse( String message, Instant timestamp, int errorCode) {
        this.message = message;
        this.timestamp = timestamp;
        this.errorCode = errorCode;
    }


    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
