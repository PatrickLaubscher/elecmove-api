package fr.elecmove.api.business.mapper;


import fr.elecmove.api.model.StationAvailability;
import org.mapstruct.*;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationAvailabilityEntityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget StationAvailability target, StationAvailability source);

}
