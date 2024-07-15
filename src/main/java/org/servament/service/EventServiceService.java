package org.servament.service;

import java.util.List;
import java.util.UUID;

import org.servament.dto.EventDTO;
import org.servament.mapper.EventServiceMapper;
import org.servament.model.Pagination;
import org.servament.model.filter.EventServiceFilter;
import org.servament.model.filter.PaginationFilter;
import org.servament.repository.IEventServiceRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventServiceService {

    private final IEventServiceRepository eventServiceRepository;

    @Inject
    public EventServiceService(IEventServiceRepository eventServiceRepository) {
        this.eventServiceRepository = eventServiceRepository;
    }

    public Uni<List<EventDTO>> list(EventServiceFilter filter) {
        return this.eventServiceRepository.list(filter).map(EventServiceMapper.INSTANCE::toPartialDTOs);
    }

    public Uni<EventDTO> find(UUID id) {
        return this.eventServiceRepository.find(id).map(EventServiceMapper.INSTANCE::toPartialDTO);
    }

    public Uni<Pagination<EventDTO>> pagination(PaginationFilter paginationFilter, EventServiceFilter filter) {
        return this.eventServiceRepository.pagination(paginationFilter, filter).map(EventServiceMapper.INSTANCE::toPaginationDTO);
    }
    
}
