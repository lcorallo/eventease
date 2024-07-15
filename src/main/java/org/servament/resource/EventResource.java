package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.servament.dto.EventDTO;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.EventServiceService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@WithSession
public class EventResource {
    
    @Inject
    private EventServiceService eventServiceService;

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

}