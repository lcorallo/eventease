package org.servament.service;

import java.util.List;
import java.util.UUID;

import org.servament.dto.OperationDTO;
import org.servament.mapper.EventOperationMapper;
import org.servament.model.Pagination;
import org.servament.model.filter.EventOperationFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventOperationRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventOperationService {

    private final IEventOperationRepository eventServiceRepository;

    @Inject
    public EventOperationService(IEventOperationRepository eventServiceRepository) {
        this.eventServiceRepository = eventServiceRepository;
    }

    public Uni<List<OperationDTO>> list(EventOperationFilter filter) {
        return this.eventServiceRepository.list(filter).map(EventOperationMapper.INSTANCE::toDTOs);
    }

    public Uni<OperationDTO> find(UUID id) {
        return this.eventServiceRepository.find(id).map(EventOperationMapper.INSTANCE::toDTO);
    }

    public Uni<Pagination<OperationDTO>> pagination(PaginationFilter paginationFilter, EventOperationFilter filter) {
        return this.eventServiceRepository.pagination(paginationFilter, filter).map(EventOperationMapper.INSTANCE::toPaginationDTO);
    }
    
    
}
