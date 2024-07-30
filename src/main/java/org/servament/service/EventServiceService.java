package org.servament.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;
import org.servament.dto.ClosingReasonDTO;
import org.servament.dto.CreateEventDTO;
import org.servament.dto.EventDTO;
import org.servament.dto.UpdateEventDTO;
import org.servament.entity.EventService;
import org.servament.exception.EventEaseException;
import org.servament.exception.event.EventClosingException;
import org.servament.exception.event.EventCompletingException;
import org.servament.exception.event.EventIllegalInputException;
import org.servament.exception.event.EventPublicationException;
import org.servament.exception.event.SchedulationException;
import org.servament.exception.eventservice.EventServiceIllegalInputException;
import org.servament.exception.eventservice.EventServiceUpdateException;
import org.servament.mapper.EventServiceMapper;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventServiceFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventServiceRepository;
import org.servament.util.EventDateTimeValidator;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ApplicationScoped
public class EventServiceService {

    private final IEventServiceRepository eventServiceRepository;

    private final Integer fetchEveryMinutes;

    private final Integer fetchWithTimeSliceMinutes;

    void onStart(@Observes StartupEvent ev, Mutiny.SessionFactory sf) {
        Multi.createFrom().ticks().every(Duration.ofMinutes(fetchEveryMinutes))
            .map((Long ignored) -> {
                EventServiceFilter scheduledFilter = new EventServiceFilter();
                scheduledFilter.setFromStartDate(Instant.now());
                scheduledFilter.setEndStartDate(Instant.now().plus(Duration.ofMinutes(fetchWithTimeSliceMinutes)));
                scheduledFilter.setStatuses(Set.of(EventStatus.PUBLISHED));
                scheduledFilter.setLimit(1000);
                try {
                    return VertxContextSupport.subscribeAndAwait(() -> sf.withTransaction(s -> this.eventServiceRepository.list(scheduledFilter)
                        .flatMap(list -> {
                            for(EventService event: list)
                                event.setStatus(EventStatus.IN_PROGRESS);
                            return Uni.createFrom().item(list);
                        })
                    ));
                } catch (Throwable e) {
                    throw new SchedulationException(e.getCause());
                }
            })
            .subscribe()
            .with((List<EventService> incomingEventsOperations) -> Log.info("New EventService in progress: " + incomingEventsOperations.size()));
    }

    @Inject
    public EventServiceService(
        IEventServiceRepository eventServiceRepository,
        @ConfigProperty(name = "app.scheduler.fetch-every-minutes") Integer fetchEveryMinutes,    
        @ConfigProperty(name = "app.scheduler.fetch-with-time-slice-minutes") Integer fetchWithTimeSliceMinutes  
    ) {
        this.eventServiceRepository = eventServiceRepository;
        this.fetchEveryMinutes = fetchEveryMinutes;
        this.fetchWithTimeSliceMinutes = fetchWithTimeSliceMinutes;
    }

    public Uni<List<EventDTO>> list(EventServiceFilter filter) {
        return this.eventServiceRepository.list(filter).map(EventServiceMapper.INSTANCE::toDTOs);
    }

    public Uni<EventDTO> find(UUID id) {
        return this.eventServiceRepository.find(id).map(EventServiceMapper.INSTANCE::toDTO);
    }

    public Uni<EventDTO> findByCode(String code) {
        return this.eventServiceRepository.findByCode(code).map(EventServiceMapper.INSTANCE::toDTO);
    }

    public Uni<Pagination<EventDTO>> pagination(PaginationFilter paginationFilter, EventServiceFilter filter) {
        return this.eventServiceRepository.pagination(paginationFilter, filter).map(EventServiceMapper.INSTANCE::toPaginationDTO);
    }

    @WithTransaction
    public Uni<EventDTO> create(CreateEventDTO createEventDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Uni<EventDTO> createEvent = Uni.createFrom().item(createEventDTO)
            .map(EventServiceMapper.INSTANCE::toEntity)
            .flatMap(this.eventServiceRepository::create)
            .map(EventServiceMapper.INSTANCE::toDTO);

        return Uni.createFrom().item(validator.validate(createEventDTO))
            .map((Set<ConstraintViolation<CreateEventDTO>> violations) -> {
                if(!violations.isEmpty())
                    throw new EventServiceIllegalInputException(
                        violations.iterator().next().getPropertyPath().toString(),
                        violations.iterator().next().getMessage()
                    );

                    try {
                        EventDateTimeValidator.validate(createEventDTO.getStartBookingDateTime(), createEventDTO.getEndBookingDateTime(), createEventDTO.getStartDateTime(), createEventDTO.getEstimatedEndDateTime());
                    } catch (EventIllegalInputException e) {
                        throw new EventServiceIllegalInputException(e.getMessage(), e.getCause());
                    }
                return null;
            })
            .flatMap(ignore -> createEvent);
    }

    @WithTransaction
    public Uni<EventDTO> patch(UUID id, UpdateEventDTO updateEventDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return Uni.createFrom().item(validator.validate(updateEventDTO))
            .flatMap((Set<ConstraintViolation<UpdateEventDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventServiceIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                )
                : Uni.createFrom().item(id)
            )
            .flatMap(eventServiceRepository::find)
            .map((EventService persistedEventService) -> {
                Instant startDateTime = updateEventDTO.getStartDateTime() != null ? updateEventDTO.getStartDateTime() : persistedEventService.getStartDateTime();
                Instant estimatedEndDateTime = updateEventDTO.getEstimatedEndDateTime() != null ? updateEventDTO.getEstimatedEndDateTime() : persistedEventService.getEstimatedEndDateTime();
                Instant startBookingDateTime = updateEventDTO.getStartBookingDateTime() != null ? updateEventDTO.getStartBookingDateTime() : persistedEventService.getStartBookingDateTime();
                Instant endBookingDateTime = updateEventDTO.getEndBookingDateTime() != null ? updateEventDTO.getEndBookingDateTime() : persistedEventService.getEndBookingDateTime();
                try {
                    EventDateTimeValidator.validate(startBookingDateTime, endBookingDateTime, startDateTime, estimatedEndDateTime);
                } catch (EventIllegalInputException e) {
                    throw new EventServiceIllegalInputException(e.getMessage(), e.getCause());
                }
                return persistedEventService;
            })
            .map((EventService persistedEventService) -> {

                if(updateEventDTO.getActivity() != null)
                    persistedEventService.setActivity(updateEventDTO.getActivity());

                if(updateEventDTO.getStartDateTime() != null)
                    persistedEventService.setStartDateTime(updateEventDTO.getStartDateTime());

                if(updateEventDTO.getEstimatedEndDateTime() != null)
                    persistedEventService.setEstimatedEndDateTime(updateEventDTO.getEstimatedEndDateTime());

                if(updateEventDTO.getStartBookingDateTime() != null)
                    persistedEventService.setStartBookingDateTime(updateEventDTO.getStartBookingDateTime());

                if(updateEventDTO.getEndBookingDateTime() != null)
                    persistedEventService.setEndBookingDateTime(updateEventDTO.getEndBookingDateTime());

                if(updateEventDTO.getLocation() != null)
                    persistedEventService.setLocation(updateEventDTO.getLocation());

                if(updateEventDTO.getCode() != null && !updateEventDTO.getCode().isEmpty() && !updateEventDTO.getCode().isBlank())
                    persistedEventService.setCode(updateEventDTO.getCode());

                if(updateEventDTO.getSupplier() != null)
                    persistedEventService.setSupplier(updateEventDTO.getSupplier());

                if(updateEventDTO.getAvailability() != null)
                    persistedEventService.setAvailability(updateEventDTO.getAvailability());

                return persistedEventService;
            })
            .map(EventServiceMapper.INSTANCE::toDTO)
            .onFailure().transform(e -> e instanceof EventEaseException
                ? e
                : new EventServiceUpdateException(id, e.getCause())
            );
    }

    @WithTransaction
    public Uni<Void> remove(UUID id) {
        return this.eventServiceRepository.remove(id);
    }
    
    @WithTransaction
    public Uni<Void> publish(UUID id) {
        return this.eventServiceRepository.find(id)
            .map((EventService persistedEventService) -> {
                Instant now = Instant.now().plusSeconds(3600L); //Plus one hour
                if(persistedEventService.getStartDateTime().isBefore(now)) throw new EventPublicationException(String.format("Event Service with the following UUID: %s, cannot be published due to the following start date. You can publish event at most from 1 hour starting from now", id));
                if(!persistedEventService.getStatus().equals(EventStatus.DRAFT)) throw new EventPublicationException(String.format("Event Service with the following UUID: %s, cannot be published because is not in DRAFT mode", id));
                
                persistedEventService.setStatus(EventStatus.PUBLISHED);
                return null;
            });
    }

    @WithTransaction
    public Uni<Void> complete(UUID id) {
        return this.eventServiceRepository.find(id)
            .map((EventService persistedEventService) -> {
                if(!persistedEventService.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventCompletingException(id);

                persistedEventService.setEndDateTime(Instant.now());
                persistedEventService.setStatus(EventStatus.COMPLETED);
                return null;
            });
    }

    @WithTransaction
    public Uni<Void> close(UUID id, ClosingReasonDTO closeEventDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return Uni.createFrom().item(validator.validate(closeEventDTO))
            .flatMap((Set<ConstraintViolation<ClosingReasonDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventServiceIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                )
                : Uni.createFrom().item(id)
            )
            .flatMap(this.eventServiceRepository::find)
            .map((EventService persistedEventService) -> {
                if(!persistedEventService.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventClosingException(id);
                
                persistedEventService.setNote(closeEventDTO.getNote());
                persistedEventService.setStatus(EventStatus.CLOSED);
                return null;
            });
    }
}
