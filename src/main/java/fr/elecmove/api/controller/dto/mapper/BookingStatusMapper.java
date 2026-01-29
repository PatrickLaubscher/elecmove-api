package fr.elecmove.api.controller.dto.mapper;

import fr.elecmove.api.controller.dto.booking_status.BookingStatusDTO;
import fr.elecmove.api.model.BookingStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING)
public interface BookingStatusMapper {

    BookingStatusDTO toDto(BookingStatus booking);
}
