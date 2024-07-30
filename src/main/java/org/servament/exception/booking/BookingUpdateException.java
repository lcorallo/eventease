package org.servament.exception.booking;

import org.servament.exception.EventEaseException;

public class BookingUpdateException extends EventEaseException {
    
    public BookingUpdateException(Long id) {
        super(String.format("An unknown error occur during update of the booking with th following %s", id));
    }

    public BookingUpdateException(Throwable cause) {
        super(cause);
    }

    public BookingUpdateException(Long id, Throwable cause) {
        super(String.format("An unknown error occur during update of the booking with th following %s", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.UPDATE_FAILURE";
    }

}
