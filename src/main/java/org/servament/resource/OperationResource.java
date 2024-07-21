package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.servament.dto.CreateOperationDTO;
import org.servament.dto.ErrorResponseDTO;
import org.servament.dto.OperationDTO;
import org.servament.exception.EventEaseException;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.exception.EventOperationNotFoundException;
import org.servament.exception.EventServiceNotFoundException;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.EventOperationService;

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
import jakarta.ws.rs.core.Response.Status;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@WithSession
public class OperationResource {

    private final EventOperationService eventOperationService;

    @Inject
    public OperationResource(EventOperationService eventOperationService) {
        this.eventOperationService = eventOperationService;
    }

    @GET
    @Path("/operations")
    public Uni<List<OperationDTO>> list() {
        return this.eventOperationService.list(null);
    }

    @GET
    @Path("/operations:paged")
    public Uni<Pagination<OperationDTO>> paginated() {
        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return eventOperationService.pagination(pagFilter, null);
    }

    @GET
    @Path("/operations/{id}")
    public Uni<OperationDTO> findById(@PathParam("id") UUID id) {
        return this.eventOperationService.find(id);
    }

    @POST
    @Path("/operation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<OperationDTO> create(CreateOperationDTO createOperationDTO) {
        return this.eventOperationService.create(createOperationDTO);

    }

    @PATCH
    @Path("/operations/{id}")
    public Uni<Response> update(@PathParam("id") UUID id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/operations/{id}")
    public Uni<Response> remove(@PathParam("id") UUID id) {
        return this.eventOperationService.remove(id)
            .map(t -> Response.noContent().build());
    }

    @ServerExceptionMapper
    public Response mapExecution(EventEaseException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getErrorCode(), e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : null);

        if(e instanceof EventOperationIllegalInputException) {
            return Response.status(Status.BAD_REQUEST).entity(error).build();
        }
        if(e instanceof EventOperationNotFoundException || e instanceof EventServiceNotFoundException) {
            return Response.status(Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

}
