package fr.elecmove.api.mapper;

import fr.elecmove.api.dto.UserResponseDTO;
import fr.elecmove.api.dto.UserCreationDTO;
import fr.elecmove.api.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreationDTO dto);
    UserResponseDTO toDto(User user);

}