package org.servament.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.servament.dto.BookingDTO;
import org.servament.dto.CreateBookingDTO;
import org.servament.dto.UpdateBookingDTO;
import org.servament.entity.Booking;
import org.servament.entity.EventService;
import org.servament.exception.EventEaseException;
import org.servament.exception.booking.BookingFullReservationException;
import org.servament.exception.booking.BookingIllegalInputException;
import org.servament.exception.booking.BookingReservationDeniedException;
import org.servament.exception.booking.BookingReservationEventNotPublishedException;
import org.servament.exception.booking.BookingUpdateException;
import org.servament.exception.eventservice.EventServiceIllegalInputException;
import org.servament.mapper.BookingMapper;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.BookingFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IBookingRepository;
import org.servament.repository.IEventServiceRepository;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ApplicationScoped
public class BookingService {

    private final IBookingRepository bookingRepository;

    private final IEventServiceRepository eventServiceRepository;

    @Inject
    public BookingService(
        IBookingRepository bookingRepository,
        IEventServiceRepository eventServiceRepository    
    ) {
        this.bookingRepository = bookingRepository;
        this.eventServiceRepository = eventServiceRepository;
    }

    @WithSession
    public Uni<List<BookingDTO>> list(BookingFilter filter) {
        return this.bookingRepository.list(filter).map(BookingMapper.INSTANCE::toDTOs);
    }

    @WithSession
    public Uni<BookingDTO> find(Long id) {
        return this.bookingRepository.find(id).map(BookingMapper.INSTANCE::toDTO);
    }

    @WithSession
    public Uni<Pagination<BookingDTO>> pagination(PaginationFilter paginationFilter, BookingFilter bookingFilter) {
        return this.bookingRepository.pagination(paginationFilter, bookingFilter).map(BookingMapper.INSTANCE::toPaginationDTO);
    }

    @WithTransaction
    public Uni<BookingDTO> create(CreateBookingDTO createBookingDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return Uni.createFrom().item(validator.validate(createBookingDTO))
            .flatMap((Set<ConstraintViolation<CreateBookingDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventServiceIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                )
                : Uni.createFrom().item(Instant.now())
            )
            .flatMap((Instant now) -> this.eventServiceRepository.find(createBookingDTO.getEvent())
                    .map((EventService eventRef) -> {
                        if(!eventRef.getStatus().equals(EventStatus.PUBLISHED))
                            throw new BookingReservationEventNotPublishedException(eventRef.getId());
                            
                        if((eventRef.getStartBookingDateTime() != null && now.isBefore(eventRef.getStartBookingDateTime())) || (eventRef.getEndBookingDateTime() != null && now.isAfter(eventRef.getEndBookingDateTime())))
                            throw new BookingReservationDeniedException(eventRef.getId());
                        return eventRef;
                    })
            )
            .flatMap((EventService eventRef) -> Uni.combine().all().unis(
                    Uni.createFrom().item(eventRef),
                    eventRef.getAvailability() != null 
                        ? this.bookingRepository.countTickets(eventRef.getId())
                        : Uni.createFrom().item(-1L)
                )
                .collectFailures()
                .asTuple()
            )
            .map((Tuple2<EventService, Long> tuple2) -> {
                if(tuple2.getItem1().getAvailability() == null || ((tuple2.getItem1().getAvailability() - tuple2.getItem2()) > 0))
                    return tuple2.getItem1();

                throw new BookingFullReservationException(tuple2.getItem1().getId());
            })
            .map((EventService eventRef) -> BookingMapper.INSTANCE.toEntity(createBookingDTO, eventRef))
            .flatMap(this.bookingRepository::create)
            .map(BookingMapper.INSTANCE::toDTO);
    }

    @WithTransaction
    public Uni<BookingDTO> patch(Long id, UpdateBookingDTO updateBookingDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return Uni.createFrom().item(validator.validate(updateBookingDTO))
            .flatMap((Set<ConstraintViolation<UpdateBookingDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new BookingIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                )
                : Uni.createFrom().item(id)
            )
            .flatMap(bookingRepository::find)
            .map((Booking persistedBooking) -> {

                if(updateBookingDTO.getConsumer() != null)
                    persistedBooking.setConsumer(updateBookingDTO.getConsumer());

                return persistedBooking;
            })
            .map(BookingMapper.INSTANCE::toDTO)
            .onFailure().transform(e -> e instanceof EventEaseException
                ? e
                : new BookingUpdateException(e.getCause())
            );
    }

    @WithTransaction
    public Uni<Void> remove(Long id) {
        return this.bookingRepository.remove(id);
    }
}
