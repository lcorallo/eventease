package org.servament.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.servament.dto.BookingDTO;
import org.servament.entity.Booking;

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

    List<BookingDTO> toDTOs(List<Booking> bookings);
}