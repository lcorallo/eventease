package org.servament.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.jboss.logmanager.Level;
import org.servament.dto.BookingDTO;
import org.servament.model.BookingStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.BookingService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
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
@WithSession
public class BookingResource {

    @Inject
    private BookingService bookingService;

    @GET
    @Path("/bookings")
    public Uni<List<BookingDTO>> list() {
        return this.bookingService.list(null).onFailure()
        .call(e -> {
            Logger.getLogger("MIO").log(Level.ERROR, null, e);

            return Uni.createFrom().item(e);
        });
    }

    @GET
    @Path("/bookings:paged")
    public Uni<Pagination<BookingDTO>> paginated() {
        List<UUID> eventIds = new ArrayList<>();
        eventIds.add(UUID.fromString("1f8a6e3e-2c6b-4e69-8a47-13543b7e1c45"));

        BookingFilter filter = new BookingFilter(Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED), null);
        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return bookingService.pagination(pagFilter, filter);
    }

    @GET
    @Path("/bookings/{id}")
    public Uni<BookingDTO> findById(@PathParam("id") Long id) {
        return this.bookingService.find(id);
    }
    

}   
