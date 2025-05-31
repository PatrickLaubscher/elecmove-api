package fr.elecmove.api.mapper;

import fr.elecmove.api.dto.UserResponseDTO;
import fr.elecmove.api.dto.UserCreationDTO;
import fr.elecmove.api.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserCreationDTO dto);

    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    UserResponseDTO toDto(User user);
}