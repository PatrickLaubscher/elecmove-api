package fr.elecmove.api.business.mapper;


import fr.elecmove.api.model.Booking;
import org.mapstruct.*;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface BookingEntityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget Booking target, Booking source);

}
