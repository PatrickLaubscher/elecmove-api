package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.car.CarCreationDTO;
import fr.elecmove.api.controller.dto.car.CarDTO;
import fr.elecmove.api.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface CarMapper {

    Car toEntity(CarCreationDTO dto);
    CarDTO toDto(Car car);

}
