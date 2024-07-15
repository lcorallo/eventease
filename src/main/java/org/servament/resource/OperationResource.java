package org.servament.resource;

import java.util.List;
import java.util.UUID;

import org.servament.dto.OperationDTO;
import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;
import org.servament.service.EventOperationService;

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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@WithSession
public class OperationResource {

    @Inject
    private EventOperationService eventOperationService;

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
    public Uni<OperationDTO> create() {
        throw new UnsupportedOperationException();
    }

    @PATCH
    @Path("/operations/{id}")
    public Uni<Response> update(@PathParam("id") UUID id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/operations/{id}")
    public Uni<Response> remove(@PathParam("id") UUID id) {
        throw new UnsupportedOperationException();
    }


}
