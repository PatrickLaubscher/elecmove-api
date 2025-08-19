package fr.elecmove.api.controller.dto.mapper;

import fr.elecmove.api.controller.dto.user.*;
import fr.elecmove.api.model.User;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserCreationDTO dto);
    User toPatchEntity(UserPatchDTO dto);
    UserResponseDTO toDto(User user);
    UserConnectedDTO toConnectedDto(User user);

    List<UserSingleDTO> toDTOList(List<User> users);

    default UserListDTO toUserListDTO(List<User> users) {
        List<UserSingleDTO> dtoList = toDTOList(users);
        return new UserListDTO(dtoList);
    }
}