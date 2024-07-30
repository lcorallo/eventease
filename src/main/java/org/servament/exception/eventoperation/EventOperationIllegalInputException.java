package org.servament.exception.eventoperation;

import org.servament.exception.EventEaseException;

public class EventOperationIllegalInputException extends EventEaseException {

    private String errorField = "";

    public EventOperationIllegalInputException(String inputField, String message) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message));
        this.errorField = inputField;
    }

    public EventOperationIllegalInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventOperationIllegalInputException(Throwable cause) {
        super(cause);
    }

    public EventOperationIllegalInputException(String inputField, String message, Throwable cause) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message), cause);
        this.errorField = inputField;
    }

    @Override
    public String getErrorCode() {
        return "EVENT_OPERATION.INVALID_INPUT." + errorField;
    }
}
