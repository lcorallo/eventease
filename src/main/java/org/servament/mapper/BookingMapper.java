package org.servament.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.servament.dto.BookingDTO;
import org.servament.dto.CreateBookingDTO;
import org.servament.entity.Booking;
import org.servament.entity.EventService;
import org.servament.model.Pagination;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper( BookingMapper.class );

    @Mapping(source = "id", target = "id")
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "event.code", target = "eventCode")
    @Mapping(source = "event.supplier", target = "supplier")
    @Mapping(source = "consumer", target = "consumer")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    BookingDTO toDTO(Booking booking);

    @Named("toDTOs")
    List<BookingDTO> toDTOs(List<Booking> bookings);

    @Mapping(source = "page", target = "page")    
    @Mapping(source = "size", target = "size")    
    @Mapping(source = "totalPages", target = "totalPages")    
    @Mapping(source = "totalSize", target = "totalSize")
    @Mapping(source = "data", target = "data", qualifiedByName = "toDTOs")    
    Pagination<BookingDTO> toPaginationDTO(Pagination<Booking> pagination);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "event", target = "event")
    @Mapping(source = "createBookingDTO.consumer", target = "consumer")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Booking toEntity(CreateBookingDTO createBookingDTO, EventService event);
}