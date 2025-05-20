package api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import api.dto.UserDTO;
import api.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getBirthdate() != null ? user.getBirthdate().toString() : null,
                user.getMobile(),
                user.getEmail(),
                user.getAddress(),
                user.getZipcode(),
                user.getCity(),
                user.getIsValidated(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null,
                user.getRoleId()
        );
    }

    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
    }
    
}
