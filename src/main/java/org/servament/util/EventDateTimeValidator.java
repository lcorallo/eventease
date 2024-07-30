package org.servament.util;

import java.time.Instant;

import org.servament.exception.EventIllegalInputException;

public class EventDateTimeValidator {

    public static void validate(Instant startBookingDateTime, Instant endBookingEndDateTime, Instant startDateTime, Instant estimatedEndDateTime) {

        if(startDateTime.isAfter(estimatedEndDateTime))
            throw new EventIllegalInputException("startDateTime_estimatedEndDateTime", "Start date time cannot be after estimated end time");
            
        if(startBookingDateTime != null && endBookingEndDateTime != null && startBookingDateTime.isAfter(endBookingEndDateTime))
            throw new EventIllegalInputException("startBookingDateTime_enddBookingEndDateTime", "Start booking date time cannot be after booking end time");

        if(startBookingDateTime != null && (startBookingDateTime.isAfter(startDateTime) || startBookingDateTime.isAfter(estimatedEndDateTime)))
            throw new EventIllegalInputException("startBookingDateTime", "Start booking date time cannot be after start date time or estimated end time of the event");
    
        if(endBookingEndDateTime != null && (endBookingEndDateTime.isAfter(startDateTime) || endBookingEndDateTime.isAfter(estimatedEndDateTime)))
            throw new EventIllegalInputException("endBookingDateTime", "End booking date time cannot be after start date time or estimated end time of the event");

    }
}
