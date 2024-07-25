package org.servament.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.exception.EventOperationNotFoundException;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventOperationRepository implements IEventOperationRepository {

    private PanacheQuery<EventOperation> buildFetchQuery(EventOperationFilter filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strBuilder = new StringBuilder("");
        
        if(filter != null) {
            //Append all filter maps and create the query
            if( filter.getStatuses() != null &&
                !filter.getStatuses().isEmpty() &&
                filter.getStatuses().size() != EventStatus.values().length
            ) {
                params.put("statuses", filter.getStatuses());
                strBuilder.append("status IN :statuses");
            }

            if (filter.getEventServiceIds() != null && !filter.getEventServiceIds().isEmpty()) {
                params.put("eventServices", filter.getEventServiceIds());
                strBuilder.append(" and event.id IN :eventServices");
            }

            if (filter.getOperators() != null && !filter.getOperators().isEmpty()) {
                params.put("operators", filter.getOperators());
                strBuilder.append(" and operator IN :operators");
            }

            if (filter.getActivities() != null && !filter.getActivities().isEmpty()) {
                params.put("activities", filter.getActivities());
                strBuilder.append(" and activity IN :activities");
            }
    
            if(strBuilder.indexOf(" and", 0) == 0) {
                strBuilder.replace(0, 4, "");
            }
        }

        return this.find(strBuilder.toString(), params);
    }

    @Override
    public Uni<Pagination<EventOperation>> pagination(PaginationFilter paginationFilter, EventOperationFilter filter) {
        final PanacheQuery<EventOperation> query = this.buildFetchQuery(filter);

        final Uni<List<EventOperation>> pagedData = query
            .page(paginationFilter.getNumPage(), paginationFilter.getPageSize())
            .list();

        final Uni<Integer> numPages = query.pageCount();
        final Uni<Long> allData = query.count();

        return pagedData.flatMap((List<EventOperation> paginatedEventOperation) -> 
                    numPages.flatMap((Integer totalPages) -> 
                        allData.map((Long totalData) -> new Pagination<EventOperation>(
                                paginatedEventOperation,
                                paginationFilter.getNumPage(),
                                paginationFilter.getPageSize(),
                                totalPages,
                                totalData.intValue()
                            )
                        )
                    )
                );
    }

    @Override
    public Uni<List<EventOperation>> list(EventOperationFilter filter) {
        return this.buildFetchQuery(filter).range(
            filter.getOffset(),
            filter.getLimit() + filter.getOffset() - 1
        ).list();
    }

    @Override
    public Uni<EventOperation> find(UUID id) {
        return this.findById(id)
            .flatMap((EventOperation eventOperation) -> eventOperation == null
                        ? Uni.createFrom().failure(new EventOperationNotFoundException(id))
                        : Uni.createFrom().item(eventOperation));
    }

    @Override
    public Uni<EventOperation> create(EventOperation incomingEntity) {
        return this.persist(incomingEntity);
    }

    @Override
    public Uni<Void> remove(UUID id) {
        return this.findById(id)
            .flatMap((EventOperation eventOperation) -> eventOperation == null
                        ? Uni.createFrom().failure(new EventOperationNotFoundException(id))
                        : Uni.createFrom().item(eventOperation)
                            .map((EventOperation eventOps) -> {
                                EventService eventParent = eventOps.getEvent();
                                List<EventOperation> newEventOperations = eventParent
                                        .getOperations()
                                        .stream()
                                        .filter((EventOperation ops) -> !ops.getId().equals(eventOperation.getId()))
                                        .collect(Collectors.toList());
                                eventParent.setOperations(newEventOperations);                            
                                return eventOps;
                            })
                            .flatMap(this::delete)
            );
    }

}
