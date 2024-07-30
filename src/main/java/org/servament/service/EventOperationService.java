package org.servament.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;
import org.servament.dto.ClosingReasonDTO;
import org.servament.dto.CreateOperationDTO;
import org.servament.dto.OperationDTO;
import org.servament.dto.UpdateOperationDTO;
import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.exception.EventEaseException;
import org.servament.exception.event.EventClosingException;
import org.servament.exception.event.EventCompletingException;
import org.servament.exception.event.EventIllegalInputException;
import org.servament.exception.event.EventPublicationException;
import org.servament.exception.event.SchedulationException;
import org.servament.exception.eventoperation.EventOperationIllegalInputException;
import org.servament.exception.eventoperation.EventOperationUpdateException;
import org.servament.exception.eventservice.EventServiceIllegalInputException;
import org.servament.mapper.EventOperationMapper;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventOperationRepository;
import org.servament.repository.IEventServiceRepository;
import org.servament.util.EventDateTimeValidator;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ApplicationScoped
public class EventOperationService {

    private final IEventOperationRepository eventOperationRepository;

    private final IEventServiceRepository eventServiceRepository;

    private final Integer fetchEveryMinutes;

    private final Integer fetchWithTimeSliceMinutes;

    void onStart(@Observes StartupEvent ev, Mutiny.SessionFactory sf) {
        Multi.createFrom().ticks().every(Duration.ofMinutes(fetchEveryMinutes))
            .map((Long ignored) -> {
                EventOperationFilter scheduledFilter = new EventOperationFilter();
                scheduledFilter.setFromStartDate(Instant.now());
                scheduledFilter.setEndStartDate(Instant.now().plus(Duration.ofMinutes(fetchWithTimeSliceMinutes)));
                scheduledFilter.setStatuses(Set.of(EventStatus.PUBLISHED));
                scheduledFilter.setLimit(1000);
                try {
                    return VertxContextSupport.subscribeAndAwait(() -> sf.withTransaction(s -> this.eventOperationRepository.list(scheduledFilter)
                        .flatMap(list -> {
                            for(EventOperation event: list)
                                event.setStatus(EventStatus.IN_PROGRESS);
                            return Uni.createFrom().item(list);
                        })
                    ));
                } catch (Throwable e) {
                    throw new SchedulationException(e.getCause());
                }
            })
            .subscribe()
            .with((List<EventOperation> incomingEventsOperations) -> Log.info("New EventOperation in progress: " + incomingEventsOperations.size()));
    }

    @Inject
    public EventOperationService(
            IEventServiceRepository eventServiceRepository,
            IEventOperationRepository eventOperationRepository,
            @ConfigProperty(name = "app.scheduler.fetch-every-minutes") Integer fetchEveryMinutes,    
            @ConfigProperty(name = "app.scheduler.fetch-with-time-slice-minutes") Integer fetchWithTimeSliceMinutes    
        ) {
        this.eventServiceRepository = eventServiceRepository;
        this.eventOperationRepository = eventOperationRepository;
        this.fetchEveryMinutes = fetchEveryMinutes;
        this.fetchWithTimeSliceMinutes = fetchWithTimeSliceMinutes;
    }

    public Uni<List<OperationDTO>> list(EventOperationFilter filter) {
        return this.eventOperationRepository.list(filter).map(EventOperationMapper.INSTANCE::toDTOs);
    }

    public Uni<OperationDTO> find(UUID id) {
        return this.eventOperationRepository.find(id).map(EventOperationMapper.INSTANCE::toDTO);
    }

    public Uni<Pagination<OperationDTO>> pagination(PaginationFilter paginationFilter, EventOperationFilter filter) {
        return this.eventOperationRepository.pagination(paginationFilter, filter)
            .map(EventOperationMapper.INSTANCE::toPaginationDTO);
    }

    @WithTransaction
    public Uni<OperationDTO> create(CreateOperationDTO createOperationDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Uni<OperationDTO> createOperation = Uni.createFrom().item(createOperationDTO)
            .flatMap(d -> this.eventServiceRepository.find(d.getEvent()))
            .flatMap((EventService eventService) -> Uni.combine().all().unis(
                    Uni.createFrom().item(createOperationDTO),
                    Uni.createFrom().item(eventService))
                .collectFailures()
                .asTuple())
            .map((Tuple2<CreateOperationDTO, EventService> tuple) -> EventOperationMapper.INSTANCE.toEntity(tuple.getItem1(), tuple.getItem2()))
            .flatMap(this.eventOperationRepository::create)
            .map(EventOperationMapper.INSTANCE::toDTO);

        
        return Uni.createFrom().item(validator.validate(createOperationDTO))
                .map((Set<ConstraintViolation<CreateOperationDTO>> violations) -> {
                    if(!violations.isEmpty())
                        throw new EventServiceIllegalInputException(
                            violations.iterator().next().getPropertyPath().toString(),
                            violations.iterator().next().getMessage()
                        );
                    
                    try {
                        EventDateTimeValidator.validate(createOperationDTO.getStartBookingDateTime(), createOperationDTO.getEndBookingDateTime(), createOperationDTO.getStartDateTime(), createOperationDTO.getEstimatedEndDateTime());
                    } catch (EventIllegalInputException e) {
                        throw new EventOperationIllegalInputException(e.getMessage(), e.getCause());
                    }
                    return null;
                })
                .flatMap(ignore -> createOperation);
    }

    @WithTransaction
    public Uni<OperationDTO> patch(UUID id, UpdateOperationDTO updateOperationDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return Uni.createFrom().item(validator.validate(updateOperationDTO))
            .flatMap((Set<ConstraintViolation<UpdateOperationDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventOperationIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                )
                : Uni.createFrom().item(updateOperationDTO)
            )
            .flatMap(incomingUpdateOperationDTO -> eventOperationRepository.find(id)
                .flatMap(persistedEventOperation -> {                    
                    Instant startDateTime = updateOperationDTO.getStartDateTime() != null ? updateOperationDTO.getStartDateTime() : persistedEventOperation.getStartDateTime();
                    Instant estimatedEndDateTime = updateOperationDTO.getEstimatedEndDateTime() != null ? updateOperationDTO.getEstimatedEndDateTime() : persistedEventOperation.getEstimatedEndDateTime();
                    Instant startBookingDateTime = updateOperationDTO.getStartBookingDateTime() != null ? updateOperationDTO.getStartBookingDateTime() : persistedEventOperation.getStartBookingDateTime();
                    Instant endBookingDateTime = updateOperationDTO.getEndBookingDateTime() != null ? updateOperationDTO.getEndBookingDateTime() : persistedEventOperation.getEndBookingDateTime();
                
                    try {
                        EventDateTimeValidator.validate(startBookingDateTime, endBookingDateTime, startDateTime, estimatedEndDateTime);
                    } catch (EventIllegalInputException e) {
                        throw new EventOperationIllegalInputException(e.getMessage(), e.getCause());
                    }

                    // Fetch event service if needed
                    Uni<EventService> eventServiceUni = incomingUpdateOperationDTO.getEvent() != null
                        ? eventServiceRepository.find(incomingUpdateOperationDTO.getEvent())
                        : Uni.createFrom().nullItem();

                    return eventServiceUni.map(persistedEventService -> {

                        if(incomingUpdateOperationDTO.getEvent() != null)
                            persistedEventOperation.setEvent(persistedEventService);
    
                        if(incomingUpdateOperationDTO.getActivity() != null)
                            persistedEventOperation.setActivity(incomingUpdateOperationDTO.getActivity());
        
                        if(incomingUpdateOperationDTO.getStartDateTime() != null)
                            persistedEventOperation.setStartDateTime(incomingUpdateOperationDTO.getStartDateTime());
        
                        if(incomingUpdateOperationDTO.getEstimatedEndDateTime() != null)
                            persistedEventOperation.setEstimatedEndDateTime(incomingUpdateOperationDTO.getEstimatedEndDateTime());

                        if(incomingUpdateOperationDTO.getStartBookingDateTime() != null)
                            persistedEventService.setStartBookingDateTime(incomingUpdateOperationDTO.getStartBookingDateTime());

                        if(incomingUpdateOperationDTO.getEndBookingDateTime() != null)
                            persistedEventService.setEndBookingDateTime(incomingUpdateOperationDTO.getEndBookingDateTime());
        
                        if(incomingUpdateOperationDTO.getLocation() != null)
                            persistedEventOperation.setLocation(incomingUpdateOperationDTO.getLocation());
        
                        if(incomingUpdateOperationDTO.getOperator() != null)
                            persistedEventOperation.setOperator(incomingUpdateOperationDTO.getOperator());
        
                        if(incomingUpdateOperationDTO.getPartecipants() != null)
                            persistedEventOperation.setPartecipants(incomingUpdateOperationDTO.getPartecipants());

                        return persistedEventOperation;
                    });
                })
                .map(EventOperationMapper.INSTANCE::toDTO)
            )
            .onFailure().transform(e -> e instanceof EventEaseException
                ? e
                : new EventOperationUpdateException(id, e.getCause())
            );

    }

    @WithTransaction
    public Uni<Void> remove(UUID id) {
        return this.eventOperationRepository.remove(id);
    }

     @WithTransaction
    public Uni<Void> publish(UUID id) {
        return this.eventOperationRepository.find(id)
            .map((EventOperation persistedEventOperation) -> {
                Instant now = Instant.now().plusSeconds(3600L); //Plus one hour
                if(persistedEventOperation.getStartDateTime().isBefore(now)) throw new EventPublicationException(String.format("Event Operation with the following UUID: %s, cannot be published due to the following start date. You can publish event at most from 1 hour starting from now", id));
                if(!persistedEventOperation.getStatus().equals(EventStatus.DRAFT)) throw new EventPublicationException(String.format("Event Operation with the following UUID: %s, cannot be published because is not in DRAFT mode", id));
                
                persistedEventOperation.setStatus(EventStatus.PUBLISHED);
                return null;
            });
    }

    @WithTransaction
    public Uni<Void> complete(UUID id) {
        return this.eventOperationRepository.find(id)
            .map((EventOperation persistedEventOperation) -> {
                if(!persistedEventOperation.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventCompletingException(id);

                persistedEventOperation.setEndDateTime(Instant.now());
                persistedEventOperation.setStatus(EventStatus.COMPLETED);
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
            .flatMap(this.eventOperationRepository::find)
            .map((EventOperation persistedEventOperation) -> {
                if(!persistedEventOperation.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventClosingException(id);
                
                persistedEventOperation.setNote(closeEventDTO.getNote());
                persistedEventOperation.setStatus(EventStatus.CLOSED);
                return null;
            });
    }
}
