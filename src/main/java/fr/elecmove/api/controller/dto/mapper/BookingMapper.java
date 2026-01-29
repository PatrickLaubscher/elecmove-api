package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.booking.BookingCreationDTO;
import fr.elecmove.api.controller.dto.booking.BookingDTO;
import fr.elecmove.api.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface BookingMapper {

    Booking toEntity(BookingCreationDTO bookingCreationDTO);
    BookingDTO toDto(Booking booking);

}
