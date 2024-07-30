package org.servament.repository;

import java.util.UUID;

import org.servament.entity.Booking;
import org.servament.model.filter.BookingFilter;

import io.smallrye.mutiny.Uni;

public interface IBookingRepository extends IBaseRepository<Booking, Long, BookingFilter> {

    public Uni<Long> countTickets(UUID eventId);

}
