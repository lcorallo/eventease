package org.servament.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.servament.entity.EventOperation;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple3;
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

        return Uni.combine().all().unis(pagedData, numPages, allData)
            .collectFailures()
            .asTuple()
            .map((Tuple3<List<EventOperation>, Integer, Long> tuple3) -> new Pagination<EventOperation>(
                tuple3.getItem1(),
                paginationFilter.getNumPage(),
                paginationFilter.getPageSize(),
                tuple3.getItem2(),
                tuple3.getItem3().intValue()));
            }

    @Override
    public Uni<List<EventOperation>> list(EventOperationFilter filter) {
        return this.buildFetchQuery(filter).list();
    }

    @Override
    public Uni<EventOperation> find(UUID id) {
        return this.findById(id);
    }

    @Override
    public Uni<EventOperation> create(EventOperation incomingEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Uni<Boolean> update(UUID id, EventOperation incomingEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Uni<Boolean> remove(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

}
