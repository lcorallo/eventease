package org.servament.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.servament.entity.EventService;
import org.servament.exception.EventServiceNotFoundException;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventServiceFilter;
import org.servament.model.filter.PaginationFilter;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple3;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventServiceRepository implements IEventServiceRepository {

    private PanacheQuery<EventService> buildFetchQuery(EventServiceFilter filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strBuilder = new StringBuilder("");

        if (filter != null) {
            // Append all filter maps and create the query
            if (filter.getStatuses() != null &&
                    !filter.getStatuses().isEmpty() &&
                    filter.getStatuses().size() != EventStatus.values().length) {
                params.put("statuses", filter.getStatuses());
                strBuilder.append("status IN :statuses");
            }

            if (filter.getCodes() != null && !filter.getCodes().isEmpty()) {
                params.put("codes", filter.getCodes());
                strBuilder.append(" and code IN :codes");
            }

            if (filter.getSuppliers() != null && !filter.getSuppliers().isEmpty()) {
                params.put("suppliers", filter.getSuppliers());
                strBuilder.append(" and supplier IN :suppliers");
            }

            if (filter.getActivities() != null && !filter.getActivities().isEmpty()) {
                params.put("activities", filter.getActivities());
                strBuilder.append(" and activity IN :activities");
            }

            if (strBuilder.indexOf(" and", 0) == 0) {
                strBuilder.replace(0, 4, "");
            }
        }

        return this.find(strBuilder.toString(), params);
    }

    @Override
    public Uni<Pagination<EventService>> pagination(PaginationFilter paginationFilter, EventServiceFilter filter) {
        final PanacheQuery<EventService> query = this.buildFetchQuery(filter);

        final Uni<List<EventService>> pagedData = query
                .page(paginationFilter.getNumPage(), paginationFilter.getPageSize())
                .list();

        final Uni<Integer> numPages = query.pageCount();
        final Uni<Long> allData = query.count();

        return Uni.combine().all().unis(pagedData, numPages, allData)
                .collectFailures()
                .asTuple()
                .map((Tuple3<List<EventService>, Integer, Long> tuple3) -> new Pagination<EventService>(
                        tuple3.getItem1(),
                        paginationFilter.getNumPage(),
                        paginationFilter.getPageSize(),
                        tuple3.getItem2(),
                        tuple3.getItem3().intValue()));
    }

    @Override
    public Uni<List<EventService>> list(EventServiceFilter filter) {
        return this.buildFetchQuery(filter).range(
            filter.getOffset(),
            filter.getLimit() + filter.getOffset() - 1
        ).list();
    }

    @Override
    public Uni<EventService> find(UUID id) {
        return this.findById(id)
                .flatMap((EventService eventService) -> eventService == null
                        ? Uni.createFrom().failure(new EventServiceNotFoundException(id))
                        : Uni.createFrom().item(eventService));
    }

    @Override
    public Uni<EventService> create(EventService incomingEntity) {
        return this.persist(incomingEntity);
    }

    @Override
    public Uni<Boolean> update(UUID id, EventService incomingEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Uni<Void> remove(UUID id) {
        return this.findById(id)
            .flatMap((EventService eventService) -> eventService == null
                        ? Uni.createFrom().failure(new EventServiceNotFoundException(id))
                        : this.delete(eventService)
            );
    }

}
