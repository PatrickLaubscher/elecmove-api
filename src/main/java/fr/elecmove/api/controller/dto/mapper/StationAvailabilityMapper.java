package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.station_availability.StationAvailabilityCreationDTO;
import fr.elecmove.api.controller.dto.station_availability.StationAvailabilityDTO;
import fr.elecmove.api.model.StationAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationAvailabilityMapper {

    StationAvailability toEntity(StationAvailabilityCreationDTO dto);
    StationAvailabilityDTO toDto(StationAvailability stationAvailability);

}
