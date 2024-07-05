package org.servament.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.entity.Booking;
import org.servament.model.BookingStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IBookingRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BookingResource {
    
    @Inject
    private IBookingRepository bookingRepository;

    @GET
    @Path("/bookings")
    public Uni<List<Booking>> list() {
        return this.bookingRepository.list(null);
    }

    @GET
    @Path("/bookings:paged")
    public Uni<Pagination<Booking>> paginated() {
        List<UUID> eventIds = new ArrayList<>();
        eventIds.add(UUID.fromString("1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45"));

        BookingFilter filter = new BookingFilter(Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED), null);
        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return bookingRepository.pagination(pagFilter, filter);
    }

    @GET
    @Path("/bookings/{id}")
    public Uni<Booking> findById(@PathParam("id") Long id) {
        return this.bookingRepository.find(id);
    }
    

}   
