package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.servament.entity.EventService;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventServiceRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/")
@ApplicationScoped
public class EventResource {
    
    @Inject
    private IEventServiceRepository eventServiceRepository;

    @GET
    @Path("/events")
    public Uni<List<EventService>> list() {
        return this.eventServiceRepository.list(null);
    }

    @GET
    @Path("/events:paged")
    public Uni<Pagination<EventService>> paginated() {

        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return this.eventServiceRepository.pagination(null, null);
    }

    @GET
    @Path("/events/{id}")
    public Uni<EventService> findById(@PathParam("id") UUID id) {
        return this.eventServiceRepository.find(id);
    }

}