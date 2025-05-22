package api.mapper;

import api.dto.UserCreationDTO;
import api.dto.UserResponseDTO;
import api.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreationDTO dto);
    UserResponseDTO toDto(User user);

}