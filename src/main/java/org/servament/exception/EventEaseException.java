package org.servament.exception;

public abstract class EventEaseException extends RuntimeException {
    protected EventEaseException() {
        super();
    }

    protected EventEaseException(String message) {
        super(message);
    }

    protected EventEaseException(Throwable cause) {
        super(cause);
    }

    protected EventEaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getErrorCode();
}
