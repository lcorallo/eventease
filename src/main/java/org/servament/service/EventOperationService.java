package org.servament.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.dto.CreateOperationDTO;
import org.servament.dto.OperationDTO;
import org.servament.dto.UpdateOperationDTO;
import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.exception.EventEaseException;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.exception.EventOperationUpdateException;
import org.servament.mapper.EventOperationMapper;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventOperationRepository;
import org.servament.repository.IEventServiceRepository;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple3;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ApplicationScoped
public class EventOperationService {

    private final IEventOperationRepository eventOperationRepository;

    private final IEventServiceRepository eventServiceRepository;

    @Inject
    public EventOperationService(
            IEventServiceRepository eventServiceRepository,
            IEventOperationRepository eventOperationRepository) {
        this.eventServiceRepository = eventServiceRepository;
        this.eventOperationRepository = eventOperationRepository;
    }

    public Uni<List<OperationDTO>> list(EventOperationFilter filter) {
        return this.eventOperationRepository.list(filter).map(EventOperationMapper.INSTANCE::toDTOs);
    }

    public Uni<OperationDTO> find(UUID id) {
        return this.eventOperationRepository.find(id).map(EventOperationMapper.INSTANCE::toDTO);
    }

    public Uni<Pagination<OperationDTO>> pagination(PaginationFilter paginationFilter, EventOperationFilter filter) {
        return this.eventOperationRepository.pagination(paginationFilter, filter).map(EventOperationMapper.INSTANCE::toPaginationDTO);
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
                .flatMap((Set<ConstraintViolation<CreateOperationDTO>> violations) -> !violations.isEmpty()
                    ? Uni.createFrom().failure(new EventOperationIllegalInputException(
                        violations.iterator().next().getPropertyPath().toString(),
                        violations.iterator().next().getMessage())
                        )
                    : createOperation
                );
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
        
                        if(incomingUpdateOperationDTO.getEndDateTime() != null)
                            persistedEventOperation.setEndDateTime(incomingUpdateOperationDTO.getEndDateTime());
        
                        if(incomingUpdateOperationDTO.getNote() != null)
                            persistedEventOperation.setNote(incomingUpdateOperationDTO.getNote());
        
                        if(incomingUpdateOperationDTO.getLocation() != null)
                            persistedEventOperation.setLocation(incomingUpdateOperationDTO.getLocation());
        
                        if(incomingUpdateOperationDTO.getOperator() != null)
                            persistedEventOperation.setOperator(incomingUpdateOperationDTO.getOperator());
        
                        if(incomingUpdateOperationDTO.getPartecipants() != null)
                            persistedEventOperation.setPartecipants(incomingUpdateOperationDTO.getPartecipants());
        
                        if(incomingUpdateOperationDTO.getStatus() != null)
                            persistedEventOperation.setStatus(incomingUpdateOperationDTO.getStatus());

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
}
