package org.servament.resource;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.servament.dto.ClosingReasonDTO;
import org.servament.dto.CreateOperationDTO;
import org.servament.dto.ErrorResponseDTO;
import org.servament.dto.OperationDTO;
import org.servament.dto.UpdateOperationDTO;
import org.servament.exception.EventClosingException;
import org.servament.exception.EventCompletingException;
import org.servament.exception.EventEaseException;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.exception.EventOperationNotFoundException;
import org.servament.exception.EventPublicationException;
import org.servament.exception.EventServiceNotFoundException;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
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
import jakarta.ws.rs.QueryParam;
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
    public Uni<List<OperationDTO>> list(
        @QueryParam("events") Set<UUID> events,
        @QueryParam("activities") Set<UUID> activities,
        @QueryParam("operators") Set<UUID> operators,
        @QueryParam("statuses") Set<EventStatus> statuses,
        @QueryParam("startFrom") Instant startFrom,
        @QueryParam("endFrom") Instant endFrom,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    ) {
        EventOperationFilter filter = new EventOperationFilter();
        filter.setEventServiceIds(events);
        filter.setActivities(activities);
        filter.setOperators(operators);
        filter.setStatuses(statuses);
        filter.setFromStartDate(startFrom);
        filter.setEndStartDate(endFrom);
        filter.setLimit(limit);
        filter.setOffset(offset);
        return this.eventOperationService.list(filter);
    }

    @GET
    @Path("/operations:paged")
    public Uni<Pagination<OperationDTO>> paginated(    
        @QueryParam("events") Set<UUID> events,
        @QueryParam("activities") Set<UUID> activities,
        @QueryParam("operators") Set<UUID> operators,
        @QueryParam("statuses") Set<EventStatus> statuses,
        @QueryParam("startFrom") Instant startFrom,
        @QueryParam("endFrom") Instant endFrom,
        @QueryParam("numPage") Integer numPage,
        @QueryParam("pageSize") Integer pageSize
    ) {
        PaginationFilter pagFilter = new PaginationFilter(pageSize, numPage);
        EventOperationFilter filter = new EventOperationFilter();
        filter.setEventServiceIds(events);
        filter.setActivities(activities);
        filter.setOperators(operators);
        filter.setStatuses(statuses);
        filter.setFromStartDate(startFrom);
        filter.setEndStartDate(endFrom);
        return eventOperationService.pagination(pagFilter, filter);
    }

    @GET
    @Path("/operations/{id}")
    public Uni<OperationDTO> findById(@PathParam("id") UUID id) {
        return this.eventOperationService.find(id);
    }

    @POST
    @Path("/operations/{id}/publish")
    public Uni<Void> publish(@PathParam("id") UUID id) {
        return this.eventOperationService.publish(id);
    }

    @POST
    @Path("/operations/{id}/complete")
    public Uni<Void> complete(@PathParam("id") UUID id) {
        return this.eventOperationService.complete(id);
    }

    @POST
    @Path("/operations/{id}/close")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Void> close(@PathParam("id") UUID id, ClosingReasonDTO closingReasonDTO) {
        return this.eventOperationService.close(id, closingReasonDTO);
    }
    
    @POST
    @Path("/operation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<OperationDTO> create(CreateOperationDTO createOperationDTO) {
        return this.eventOperationService.create(createOperationDTO);

    }

    @PATCH
    @Path("/operations/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<OperationDTO> update(@PathParam("id") UUID id, UpdateOperationDTO updateOperationDTO) {
        return this.eventOperationService.patch(id, updateOperationDTO);
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
        if(e instanceof EventPublicationException || e instanceof EventClosingException || e instanceof EventCompletingException) {
            return Response.status(Status.FORBIDDEN).entity(error).build();
        }
        if(e instanceof EventOperationNotFoundException || e instanceof EventServiceNotFoundException) {
            return Response.status(Status.NOT_FOUND).entity(error).build();
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

}
