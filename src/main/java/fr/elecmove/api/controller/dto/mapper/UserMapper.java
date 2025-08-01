package fr.elecmove.api.controller.dto.mapper;

import fr.elecmove.api.controller.dto.user.UserCreationDTO;
import fr.elecmove.api.controller.dto.user.UserListDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import fr.elecmove.api.controller.dto.user.UserSingleDTO;
import fr.elecmove.api.model.User;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserCreationDTO dto);
    UserResponseDTO toDto(User user);
    List<UserSingleDTO> toDTOList(List<User> users);

    default UserListDTO toUserListDTO(List<User> users) {
        List<UserSingleDTO> dtoList = toDTOList(users);
        return new UserListDTO(dtoList);
    }
}