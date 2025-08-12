package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.location_station.LocationStationCreationDTO;
import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;
import fr.elecmove.api.model.LocationStation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationStation toEntity(LocationStationCreationDTO dto);
    LocationStationDTO toDto(LocationStation car);

}
