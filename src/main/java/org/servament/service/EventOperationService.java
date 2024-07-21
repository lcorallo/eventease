package org.servament.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.dto.CreateOperationDTO;
import org.servament.dto.OperationDTO;
import org.servament.entity.EventService;
import org.servament.exception.EventOperationIllegalInputException;
import org.servament.mapper.EventOperationMapper;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventOperationRepository;
import org.servament.repository.IEventServiceRepository;

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

}
