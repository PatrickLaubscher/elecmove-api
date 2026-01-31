package fr.elecmove.api.business.mapper;

import fr.elecmove.api.model.Review;
import org.mapstruct.*;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface ReviewEntityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget Review target, Review source);
}
