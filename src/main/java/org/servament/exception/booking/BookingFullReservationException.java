package org.servament.exception.booking;

import java.util.UUID;

import org.servament.exception.EventEaseException;

public class BookingFullReservationException extends EventEaseException {

    public BookingFullReservationException(UUID id) {
        super(String.format("You cannot create a booking for the event with ID: %s, because the event has achieved the maximum numbers of reservations", id));
    }

    public BookingFullReservationException(Throwable cause) {
        super(cause);
    }

    public BookingFullReservationException(Long id, Throwable cause) {
        super(String.format("You cannot create a booking for the event with ID: %s, because the event has achieved the maximum numbers of reservations", id), cause);
    }

    @Override
    public String getErrorCode() {
        return "BOOKING.RESERVATION_FULFILLED";
    }

}