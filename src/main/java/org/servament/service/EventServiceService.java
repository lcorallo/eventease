package org.servament.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.dto.ClosingReasonDTO;
import org.servament.dto.CreateEventDTO;
import org.servament.dto.EventDTO;
import org.servament.dto.UpdateEventDTO;
import org.servament.entity.EventService;
import org.servament.exception.EventEaseException;
import org.servament.exception.EventServiceClosingException;
import org.servament.exception.EventServiceCompletingException;
import org.servament.exception.EventServiceIllegalInputException;
import org.servament.exception.EventServicePublicationException;
import org.servament.exception.EventServiceUpdateException;
import org.servament.mapper.EventServiceMapper;
import org.servament.model.EventStatus;
import org.servament.model.Pagination;
import org.servament.model.filter.EventServiceFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventServiceRepository;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ApplicationScoped
public class EventServiceService {

    private final IEventServiceRepository eventServiceRepository;

    @Inject
    public EventServiceService(IEventServiceRepository eventServiceRepository) {
        this.eventServiceRepository = eventServiceRepository;
    }

    public Uni<List<EventDTO>> list(EventServiceFilter filter) {
        return this.eventServiceRepository.list(filter).map(EventServiceMapper.INSTANCE::toDTOs);
    }

    public Uni<EventDTO> find(UUID id) {
        return this.eventServiceRepository.find(id).map(EventServiceMapper.INSTANCE::toDTO);
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
            .flatMap((Set<ConstraintViolation<CreateEventDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventServiceIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                    )
                : createEvent
            );
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
                : Uni.createFrom().item(updateEventDTO)
            )
            .flatMap(incomingUpdateEventDTO -> eventServiceRepository.find(id))
            .map((EventService persistedEventService) -> {

                if(updateEventDTO.getActivity() != null)
                    persistedEventService.setActivity(updateEventDTO.getActivity());

                if(updateEventDTO.getStartDateTime() != null)
                    persistedEventService.setStartDateTime(updateEventDTO.getStartDateTime());

                if(updateEventDTO.getEstimatedEndDateTime() != null)
                    persistedEventService.setEstimatedEndDateTime(updateEventDTO.getEstimatedEndDateTime());

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
                if(persistedEventService.getStartDateTime().isBefore(now)) throw new EventServicePublicationException(String.format("Event Service with the following UUID: %s, cannot be published due to the following start date. You can publish event at most from 1 hour starting from now", id));
                if(!persistedEventService.getStatus().equals(EventStatus.DRAFT)) throw new EventServicePublicationException(String.format("Event Service with the following UUID: %s, cannot be published because is not in DRAFT mode", id));
                
                persistedEventService.setStatus(EventStatus.PUBLISHED);
                return null;
            });
    }

    @WithTransaction
    public Uni<Void> complete(UUID id) {
        return this.eventServiceRepository.find(id)
            .map((EventService persistedEventService) -> {
                if(!persistedEventService.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventServiceCompletingException(id);

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
                if(!persistedEventService.getStatus().equals(EventStatus.IN_PROGRESS)) throw new EventServiceClosingException(id);
                
                persistedEventService.setNote(closeEventDTO.getNote());
                persistedEventService.setStatus(EventStatus.CLOSED);
                return null;
            });
    }
}
