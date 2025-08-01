package fr.elecmove.api.service;


import java.util.List;

import fr.elecmove.api.controller.dto.user.UserCreationDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import fr.elecmove.api.model.User;

public interface UserService {
    UserResponseDTO createUser(UserCreationDTO userDto);;
    List<User> getAllUsers();
}