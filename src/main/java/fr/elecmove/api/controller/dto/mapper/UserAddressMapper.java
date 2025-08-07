package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.user_address.UserAddressCreationDTO;
import fr.elecmove.api.controller.dto.user_address.UserAddressDTO;
import fr.elecmove.api.controller.dto.user_address.UserAddressListDTO;
import fr.elecmove.api.model.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface UserAddressMapper {

    UserAddress toEntity(UserAddressCreationDTO dto);
    UserAddressDTO toDto(UserAddress userAddress);

    List<UserAddressDTO> toDTOList(List<UserAddress> userAddresses);

    default UserAddressListDTO toUserAddressListDTO(List<UserAddress> userAddresses) {
        List<UserAddressDTO> dtoList = toDTOList(userAddresses);
        return new UserAddressListDTO(dtoList);
    }

}
