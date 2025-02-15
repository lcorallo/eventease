package org.servament.resource;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.dto.ClosingReasonDTO;
import org.servament.dto.CreateEventDTO;
import org.servament.dto.EventDTO;
import org.servament.dto.UpdateEventDTO;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventServiceFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.EventServiceService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@RequestScoped
public class EventResource {
    
    private final EventServiceService eventServiceService;

    @Inject
    public EventResource(EventServiceService eventServiceService) {
        this.eventServiceService = eventServiceService;
    }

    @GET
    @Path("/events")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<EventDTO>> list(
        @QueryParam("codes") Set<String> codes,
        @QueryParam("activities") Set<UUID> activities,
        @QueryParam("suppliers") Set<UUID> suppliers,
        @QueryParam("statuses") Set<EventStatus> statuses,
        @QueryParam("startFrom") Instant startFrom,
        @QueryParam("endFrom") Instant endFrom,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    ) {
        EventServiceFilter filter = new EventServiceFilter();
        filter.setCodes(codes);
        filter.setActivities(activities);
        filter.setSuppliers(suppliers);
        filter.setStatuses(statuses);
        filter.setFromStartDate(startFrom);
        filter.setEndStartDate(endFrom);
        filter.setLimit(limit);
        filter.setOffset(offset);
        return this.eventServiceService.list(filter);
    }

    @GET
    @Path("/events:paged")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Pagination<EventDTO>> paginated(
        @QueryParam("codes") Set<String> codes,
        @QueryParam("activities") Set<UUID> activities,
        @QueryParam("suppliers") Set<UUID> suppliers,
        @QueryParam("statuses") Set<EventStatus> statuses,
        @QueryParam("startFrom") Instant startFrom,
        @QueryParam("endFrom") Instant endFrom,
        @QueryParam("numPage") Integer numPage,
        @QueryParam("pageSize") Integer pageSize
    ) {
        PaginationFilter pagFilter = new PaginationFilter(pageSize, numPage);
        EventServiceFilter filter = new EventServiceFilter();
        filter.setCodes(codes);
        filter.setActivities(activities);
        filter.setSuppliers(suppliers);
        filter.setStatuses(statuses);
        filter.setFromStartDate(startFrom);
        filter.setEndStartDate(endFrom);
        return this.eventServiceService.pagination(pagFilter, filter);
    }

    @GET
    @Path("/events/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EventDTO> findById(@PathParam("id") UUID id) {
        return this.eventServiceService.find(id);
    }
    
    @GET
    @Path("/events/code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EventDTO> findByCode(@PathParam("code") String code) {
        return this.eventServiceService.findByCode(code);
    }
    
    @HEAD
    @Path("/events/{id}")
    public Uni<Response> existsById(@PathParam("id") UUID id) {
        return this.eventServiceService.find(id)
            .map((EventDTO ignored) -> Response.ok().build());
    }
 
    @HEAD
    @Path("/events/code/{code}")
    public Uni<Response> existsByCode(@PathParam("code") String code) {
        return this.eventServiceService.findByCode(code)
            .map((EventDTO ignored) -> Response.ok().build());
    }

    @POST
    @Path("/events/{id}/publish")
    public Uni<Void> publish(@PathParam("id") UUID id) {
        return this.eventServiceService.publish(id);
    }

    @POST
    @Path("/events/{id}/complete")
    public Uni<Void> complete(@PathParam("id") UUID id) {
        return this.eventServiceService.complete(id);
    }

    @POST
    @Path("/events/{id}/close")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Void> close(@PathParam("id") UUID id, ClosingReasonDTO closingReasonDTO) {
        return this.eventServiceService.close(id, closingReasonDTO);
    }
    
    @POST
    @Path("/event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EventDTO> create(CreateEventDTO createEventDTO) {
        return this.eventServiceService.create(createEventDTO);
    }

    @PATCH
    @Path("/events/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<EventDTO> update(@PathParam("id") UUID id, UpdateEventDTO updateEventDTO) {
        return this.eventServiceService.patch(id, updateEventDTO);
    }

    @DELETE
    @Path("/events/{id}")
    public Uni<Response> remove(@PathParam("id") UUID id) {
        return this.eventServiceService.remove(id)
            .map(t -> Response.noContent().build());
    }

}