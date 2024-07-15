package org.servament.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.servament.dto.OperationDTO;
import org.servament.entity.EventOperation;
import org.servament.model.Pagination;

@Mapper
public interface EventOperationMapper {

    EventOperationMapper INSTANCE = Mappers.getMapper( EventOperationMapper.class );

    @Mapping(source = "id", target = "id")
    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "startDateTime", target = "startDateTime")
    @Mapping(source = "estimatedEndDateTime", target = "estimatedEndDateTime")
    @Mapping(source = "endDateTime", target = "endDateTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "operator", target = "operator")
    @Mapping(source = "partecipants", target = "partecipants")
    @Mapping(source = "event.id", target = "event")
    OperationDTO toDTO(EventOperation operation);

    @Named("toDTOs")
    List<OperationDTO> toDTOs(List<EventOperation> event);

    @Mapping(source = "page", target = "page")    
    @Mapping(source = "size", target = "size")    
    @Mapping(source = "totalPages", target = "totalPages")    
    @Mapping(source = "totalSize", target = "totalSize")
    @Mapping(source = "data", target = "data", qualifiedByName = "toDTOs")    
    Pagination<OperationDTO> toPaginationDTO(Pagination<EventOperation> pagination);
    
}
