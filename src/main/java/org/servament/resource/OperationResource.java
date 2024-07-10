package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.servament.entity.EventOperation;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventOperationRepository;

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
public class OperationResource {

    @Inject
    private IEventOperationRepository eventOperationRepository;

    @GET
    @Path("/operations")
    public Uni<List<EventOperation>> list() {
        return this.eventOperationRepository.list(null);        
    }

    @GET
    @Path("/operations:paged")
    public Uni<Pagination<EventOperation>> paginated() {
        PaginationFilter pagFilter = new PaginationFilter(5, 0);

        return eventOperationRepository.pagination(pagFilter, null);
    }

    @GET
    @Path("/operations/{id}")
    public Uni<EventOperation> findById(@PathParam("id") UUID id) {
        return this.eventOperationRepository.find(id);
    }

}
