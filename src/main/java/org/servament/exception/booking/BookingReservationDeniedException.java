package org.servament.exception.booking;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class BookingReservationDeniedException extends EventEaseException {

    public BookingReservationDeniedException(UUID id) {
        super(String.format("You cannot create a booking for the event with ID: %s, check the time period when you can create a booking", id));
    }

    public BookingReservationDeniedException(Throwable cause) {
        super(cause);
    }

    public BookingReservationDeniedException(Long id, Throwable cause) {
        super(String.format("You cannot create a booking for the event with ID: %s, check the time period when you can create a booking", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.RESERVATION_DENIED";
    }

}