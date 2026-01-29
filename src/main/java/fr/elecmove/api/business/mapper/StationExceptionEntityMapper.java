package fr.elecmove.api.business.mapper;


import fr.elecmove.api.model.StationException;
import org.mapstruct.*;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationExceptionEntityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget StationException target, StationException source);

}
