package org.servament.exception.booking;

import org.servament.exception.EventEaseException;

public class BookingIllegalInputException extends EventEaseException {

    private String errorField = "";

    public BookingIllegalInputException(String inputField, String message) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message));
        this.errorField = inputField;
    }

    public BookingIllegalInputException(Throwable cause) {
        super(cause);
    }

    public BookingIllegalInputException(String inputField, String message, Throwable cause) {
        super(String.format("Input DTO field: %s is invalid for the following reason: %s", inputField, message), cause);
        this.errorField = inputField;
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.INVALID_INPUT." + errorField;
    }
}
