package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.station.StationCreationDTO;
import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.controller.dto.station.StationSingleDTO;
import fr.elecmove.api.controller.dto.station_exception.StationExceptionDTO;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.StationException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationMapper {

    Station toEntity(StationCreationDTO dto);
    StationDTO toDto(Station station);
    StationSingleDTO toSingleDto(Station station);

    @Mapping(source = "station.id", target = "stationId")
    StationExceptionDTO toStationExceptionDto(StationException stationException);

    // Méthode par défaut qui utilise le mapping de base
    default StationSingleDTO toSingleDtoWithAvailability(Station station, boolean availableAtGivenSlot) {
        StationSingleDTO dto = toSingleDto(station);
        dto.setAvailableAtGivenSlot(availableAtGivenSlot);
        return dto;
    }

}
