package org.servament.exception;

public class EventIllegalInputException extends EventEaseException {

    private String errorField = "";

    public EventIllegalInputException(String inputField, String message) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message));
        this.errorField = inputField;
    }

    public EventIllegalInputException(Throwable cause) {
        super(cause);
    }

    public EventIllegalInputException(String inputField, String message, Throwable cause) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message), cause);
        this.errorField = inputField;
    }

    @Override
    public String getErrorCode() {
        return "EVENT.INVALID_INPUT." + errorField;
    }
}
