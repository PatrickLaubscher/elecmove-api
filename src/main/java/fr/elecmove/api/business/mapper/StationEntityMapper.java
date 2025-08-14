package fr.elecmove.api.business.mapper;


import fr.elecmove.api.model.Station;
import org.mapstruct.*;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface StationEntityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget Station target, Station source);

}


