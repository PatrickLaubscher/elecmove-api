package api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.UserDTO;
import api.mapper.UserMapper;
import api.model.User;
import api.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    // Initialize the UserService
    private final UserService userService;

    // Constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return UserMapper.toDTOList(users);
    }


    // Get user by ID
    // Get user by username
    // Get user by email
    // Create user
    // Update user
    // Delete user



}
