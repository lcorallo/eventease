package org.servament.exception.booking;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class BookingReservationEventNotPublishedException extends EventEaseException {
    
    public BookingReservationEventNotPublishedException(UUID id) {
        super(String.format("You cannot create a booking for the event with ID: %s because is not PUBLISHED", id));
    }

    public BookingReservationEventNotPublishedException(Throwable cause) {
        super(cause);
    }

    public BookingReservationEventNotPublishedException(Long id, Throwable cause) {
        super(String.format("You cannot create a booking for the event with ID: %s because is not PUBLISHED", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.EVENT_NOT_PUBLISHED";
    }

}