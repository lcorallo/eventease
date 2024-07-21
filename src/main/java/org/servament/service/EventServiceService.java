package org.servament.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.dto.CreateEventDTO;
import org.servament.dto.EventDTO;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.mapper.EventServiceMapper;
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
            .flatMap(this.eventServiceRepository::persist)
            .map(EventServiceMapper.INSTANCE::toDTO);

        return Uni.createFrom().item(validator.validate(createEventDTO))
            .flatMap((Set<ConstraintViolation<CreateEventDTO>> violations) -> !violations.isEmpty()
                ? Uni.createFrom().failure(new EventOperationIllegalInputException(
                    violations.iterator().next().getPropertyPath().toString(),
                    violations.iterator().next().getMessage())
                    )
                : createEvent
            );
    }
    
}
