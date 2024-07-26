package org.servament.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.servament.entity.Booking;
import org.servament.exception.BookingNotFoundException;
import org.servament.model.BookingStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookingRepository implements IBookingRepository {

    private PanacheQuery<Booking> buildFetchQuery(BookingFilter filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strBuilder = new StringBuilder("");
        
        if(filter != null) {
            //Append all filter maps and create the query
            if( filter.getStatuses() != null &&
                !filter.getStatuses().isEmpty() &&
                filter.getStatuses().size() != BookingStatus.values().length
            ) {
                params.put("statuses", filter.getStatuses());
                strBuilder.append("status IN :statuses");
            }
    
            if (filter.getEvents() != null && !filter.getEvents().isEmpty()) {
                params.put("eventServices", filter.getEvents());
                strBuilder.append(" and event.id IN :eventServices");
            }
    
            if(strBuilder.indexOf(" and", 0) == 0) {
                strBuilder.replace(0, 4, "");
            }
        }

        return this.find(strBuilder.toString(), params);
    }

    @Override
    public Uni<Pagination<Booking>> pagination(PaginationFilter paginationFilter, BookingFilter filter) {
        final PanacheQuery<Booking> query = this.buildFetchQuery(filter);

        final Uni<List<Booking>> pagedData = query
            .page(paginationFilter.getNumPage(), paginationFilter.getPageSize())
            .list();

        final Uni<Integer> numPages = query.pageCount();
        final Uni<Long> allData = query.count();

        return pagedData.flatMap((List<Booking> paginatedBookings) -> 
                    numPages.flatMap((Integer totalPages) -> 
                        allData.map((Long totalData) -> new Pagination<Booking>(
                                paginatedBookings,
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
    public Uni<List<Booking>> list(BookingFilter filter) {
        return this.buildFetchQuery(filter).list();
    }

    @Override
    public Uni<Booking> find(Long id) {
        return this.findById(id)
                    .flatMap((Booking eventService) -> eventService == null
                    ? Uni.createFrom().failure(new BookingNotFoundException(id))
                    : Uni.createFrom().item(eventService));
    }

    @Override
    public Uni<Booking> create(Booking incomingEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }


    @Override
    public Uni<Void> remove(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

}
