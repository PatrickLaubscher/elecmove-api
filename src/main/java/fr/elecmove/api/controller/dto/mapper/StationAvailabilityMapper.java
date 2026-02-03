package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.station_exception.StationExceptionCreationDTO;
import fr.elecmove.api.controller.dto.station_exception.StationExceptionDTO;
import fr.elecmove.api.model.StationException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationAvailabilityMapper {

    StationException toEntity(StationExceptionCreationDTO dto);

    @Mapping(source = "station.id", target = "stationId")
    StationExceptionDTO toDto(StationException stationException);

}
