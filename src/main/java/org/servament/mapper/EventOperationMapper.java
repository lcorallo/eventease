package org.servament.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.servament.dto.CreateOperationDTO;
import org.servament.dto.OperationDTO;
import org.servament.dto.UpdateOperationDTO;
import org.servament.entity.EventOperation;
import org.servament.entity.EventService;
import org.servament.model.Pagination;

@Mapper
public interface EventOperationMapper {

    EventOperationMapper INSTANCE = Mappers.getMapper(EventOperationMapper.class);

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

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createOperationDTO.activity", target = "activity")
    @Mapping(source = "createOperationDTO.startDateTime", target = "startDateTime")
    @Mapping(source = "createOperationDTO.estimatedEndDateTime", target = "estimatedEndDateTime")
    @Mapping(source = "createOperationDTO.endDateTime", target = "endDateTime")
    @Mapping(target = "status", constant = "DRAFT")
    @Mapping(source = "createOperationDTO.note", target = "note")
    @Mapping(source = "createOperationDTO.location", target = "location")
    @Mapping(source = "createOperationDTO.operator", target = "operator")
    @Mapping(source = "createOperationDTO.partecipants", target = "partecipants")
    @Mapping(source = "event", target = "event")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    EventOperation toEntity(CreateOperationDTO createOperationDTO, EventService event);

}
