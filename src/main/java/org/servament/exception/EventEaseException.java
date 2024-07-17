package org.servament.exception;

public abstract class EventEaseException extends RuntimeException {
    public EventEaseException() {
        super();
    }

    public EventEaseException(String message) {
        super(message);
    }

    public EventEaseException(Throwable cause) {
        super(cause);
    }

    public EventEaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getErrorCode();
}
