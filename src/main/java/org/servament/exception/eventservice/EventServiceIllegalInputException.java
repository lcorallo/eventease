package org.servament.exception.eventservice;

import org.servament.exception.EventEaseException;

public class EventServiceIllegalInputException extends EventEaseException {

    private String errorField = "";

    public EventServiceIllegalInputException(String inputField, String message) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message));
        this.errorField = inputField;
    }

    public EventServiceIllegalInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventServiceIllegalInputException(Throwable cause) {
        super(cause);
    }

    public EventServiceIllegalInputException(String inputField, String message, Throwable cause) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message), cause);
        this.errorField = inputField;
    }

    @Override
    public String getErrorCode() {
        return "EVENT_SERVICE.INVALID_INPUT." + errorField;
    }
}
