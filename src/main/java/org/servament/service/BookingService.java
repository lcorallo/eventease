package org.servament.service;

import java.util.List;

import org.servament.dto.BookingDTO;
import org.servament.mapper.BookingMapper;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IBookingRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BookingService {

    private final IBookingRepository bookingRepository;

    @Inject
    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Uni<List<BookingDTO>> list(BookingFilter filter) {
        return this.bookingRepository.list(filter).map(BookingMapper.INSTANCE::toDTOs);
    }

    public Uni<BookingDTO> find(Long id) {
        return this.bookingRepository.find(id).map(BookingMapper.INSTANCE::toDTO);
    }

    public Uni<Pagination<BookingDTO>> pagination(PaginationFilter paginationFilter, BookingFilter bookingFilter) {
        return this.bookingRepository.pagination(paginationFilter, bookingFilter).map(BookingMapper.INSTANCE::toPaginationDTO);
    }
}
