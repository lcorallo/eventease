package org.servament.exception;

public class BookingNotFoundException extends EventEaseException {

    public BookingNotFoundException(Long id) {
        super(String.format("Does not exists any booking with the following ID: %s", id));
    }

    public BookingNotFoundException(Throwable cause) {
        super(cause);
    }

    public BookingNotFoundException(Long id, Throwable cause) {
        super(String.format("Does not exists any booking with the following ID: %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.NOT_FOUND";
    }

}
