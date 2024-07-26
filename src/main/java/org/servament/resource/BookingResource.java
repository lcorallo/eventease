package org.servament.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.servament.dto.BookingDTO;
import org.servament.dto.ErrorResponseDTO;
import org.servament.exception.BookingNotFoundException;
import org.servament.exception.EventEaseException;
import org.servament.model.BookingStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.BookingService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@WithSession
public class BookingResource {

    private final BookingService bookingService;

    @Inject
    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GET
    @Path("/bookings")
    public Uni<List<BookingDTO>> list(
        @QueryParam("events") Set<UUID> events,
        @QueryParam("statuses") Set<BookingStatus> statuses,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    ) {
        BookingFilter bookingFilter = new BookingFilter();
        bookingFilter.setEvents(new ArrayList<>(events));
        bookingFilter.setStatuses(statuses);
        bookingFilter.setLimit(limit);
        bookingFilter.setOffset(offset);        
        return this.bookingService.list(bookingFilter);
    }

    @GET
    @Path("/bookings:paged")
    public Uni<Pagination<BookingDTO>> paginated(
        @QueryParam("events") Set<UUID> events,
        @QueryParam("statuses") Set<BookingStatus> statuses,
        @QueryParam("numPage") Integer numPage,
        @QueryParam("pageSize") Integer pageSize
    ) {
        PaginationFilter pagFilter = new PaginationFilter(pageSize, numPage);
        BookingFilter filter = new BookingFilter();
        filter.setEvents(new ArrayList<>(events));
        filter.setStatuses(statuses);
        return bookingService.pagination(pagFilter, filter);
    }

    @GET
    @Path("/bookings/{id}")
    public Uni<BookingDTO> findById(@PathParam("id") Long id) {
        return this.bookingService.find(id);
    }

    @POST
    @Path("/booking")
    public Uni<BookingDTO> create() {
        throw new UnsupportedOperationException();
    }

    @PATCH
    @Path("/bookings/{id}")
    public Uni<Response> update(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/bookings/{id}")
    public Uni<Response> remove(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }
    
    @ServerExceptionMapper
    public Response mapExecution(EventEaseException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getErrorCode(), e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : null);

        if(e instanceof BookingNotFoundException) {
            return Response.status(Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

}   
