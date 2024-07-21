package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.servament.dto.CreateEventDTO;
import org.servament.dto.ErrorResponseDTO;
import org.servament.dto.EventDTO;
import org.servament.exception.EventEaseException;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.exception.EventOperationNotFoundException;
import org.servament.exception.EventServiceNotFoundException;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.EventServiceService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@WithSession
public class EventResource {
    
    private final EventServiceService eventServiceService;

    @Inject
    public EventResource(EventServiceService eventServiceService) {
        this.eventServiceService = eventServiceService;
    }

    @GET
    @Path("/events")
    public Uni<List<EventDTO>> list() {
        return this.eventServiceService.list(null);
    }

    @GET
    @Path("/events:paged")
    public Uni<Pagination<EventDTO>> paginated() {

        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return this.eventServiceService.pagination(pagFilter, null);
    }

    @GET
    @Path("/events/{id}")
    public Uni<EventDTO> findById(@PathParam("id") UUID id) {
        return this.eventServiceService.find(id);
    }
    
    @POST
    @Path("/event")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<EventDTO> create(CreateEventDTO createEventDTO) {
        return this.eventServiceService.create(createEventDTO);
    }

    @PATCH
    @Path("/events/{id}")
    public Uni<Response> update(@PathParam("id") UUID id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/events/{id}")
    public Uni<Response> remove(@PathParam("id") UUID id) {
        return this.eventServiceService.remove(id)
            .map(t -> Response.noContent().build());
    }

    @ServerExceptionMapper
    public Response mapExecution(EventEaseException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getErrorCode(), e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : null);
        
        if(e instanceof EventOperationIllegalInputException) {
            return Response.status(Status.BAD_REQUEST).entity(error).build();
        }
        if(e instanceof EventServiceNotFoundException || e instanceof EventOperationNotFoundException) {
            return Response.status(Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

}