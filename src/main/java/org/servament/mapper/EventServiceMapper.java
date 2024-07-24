package org.servament.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.servament.dto.CreateEventDTO;
import org.servament.dto.EventDTO;
import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.model.Pagination;

@Mapper
public interface EventServiceMapper {

    EventServiceMapper INSTANCE = Mappers.getMapper( EventServiceMapper.class );

    @Named("operationsId")
    default Set<UUID> getOperationIds(List<EventOperation> operations) {
        return operations.stream().map(o -> o.getId()).collect(Collectors.toSet());
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "startDateTime", target = "startDateTime")
    @Mapping(source = "estimatedEndDateTime", target = "estimatedEndDateTime")
    @Mapping(source = "endDateTime", target = "endDateTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "supplier", target = "supplier")
    @Mapping(source = "availability", target = "availability")
    @Mapping(target = "operations", source = "event.operations", qualifiedByName = "operationsId")
    EventDTO toDTO(EventService event);

    @Named("toDTOs")
    List<EventDTO> toDTOs(List<EventService> event);
    
    @Mapping(source = "page", target = "page")    
    @Mapping(source = "size", target = "size")    
    @Mapping(source = "totalPages", target = "totalPages")    
    @Mapping(source = "totalSize", target = "totalSize")
    @Mapping(source = "data", target = "data", qualifiedByName = "toDTOs")    
    Pagination<EventDTO> toPaginationDTO(Pagination<EventService> pagination);

    
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "startDateTime", target = "startDateTime")
    @Mapping(source = "estimatedEndDateTime", target = "estimatedEndDateTime")
    @Mapping(target = "endDateTime", ignore = true)
    @Mapping(target = "status", constant = "DRAFT")
    @Mapping(target = "note", ignore = true)
    @Mapping(source = "location", target = "location")
    @Mapping(source = "supplier", target = "supplier")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "availability", target = "availability")
    @Mapping(target = "operations", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    EventService toEntity(CreateEventDTO createEventDTO);
}
