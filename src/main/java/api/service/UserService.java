package api.service;


import java.util.List;

import api.dto.UserCreationDTO;
import api.dto.UserResponseDTO;
import api.model.User;

public interface UserService {
    User createUser(UserCreationDTO userDto);
    List<User> getAllUsers();
}