package org.servament.service;

import java.util.List;
import java.util.UUID;

import org.servament.dto.CreateOperationDTO;
import org.servament.dto.OperationDTO;
import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.exception.EventServiceNotFoundException;
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
        return this.eventServiceRepository.findById(createOperationDTO.getEvent())
                .onFailure().transform(f -> new EventServiceNotFoundException(createOperationDTO.getEvent(), f.getCause()))
                .flatMap((EventService eventService) -> Uni.combine().all().unis(
                        Uni.createFrom().item(createOperationDTO),
                        Uni.createFrom().item(eventService))
                        .collectFailures()
                        .asTuple())
                .map((Tuple2<CreateOperationDTO, EventService> tuple) -> EventOperationMapper.INSTANCE.toEntity(tuple.getItem1(), tuple.getItem2()))
                .flatMap((EventOperation eventOperation) -> this.eventOperationRepository.persist(eventOperation))
                .map(EventOperationMapper.INSTANCE::toDTO);
    }

}
