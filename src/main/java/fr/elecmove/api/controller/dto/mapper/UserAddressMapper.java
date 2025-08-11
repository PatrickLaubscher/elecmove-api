package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.user_address.UserAddressCreationDTO;
import fr.elecmove.api.controller.dto.user_address.UserAddressDTO;
import fr.elecmove.api.model.UserAddress;
import org.mapstruct.*;


@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface UserAddressMapper {

    UserAddress toEntity(UserAddressCreationDTO dto);
    UserAddressDTO toDto(UserAddress userAddress);

}
