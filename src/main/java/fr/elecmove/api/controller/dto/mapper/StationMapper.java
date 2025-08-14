package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.station.StationCreationDTO;
import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.model.Station;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationMapper {

    Station toEntity(StationCreationDTO dto);
    StationDTO toDto(Station station);

}
