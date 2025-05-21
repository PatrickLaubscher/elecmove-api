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
@RequestMapping("/users")
public class UserController {

    // Initialize the UserService
    private final UserService userService;
    private final UserMapper userMapper;

    // Constructor
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    // This method retrieves all users from the database and returns them as a list of UserDTO objects.
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return userMapper.usersToUserDTOs(users);
    }


    // Get user by ID
    // Get user by username
    // Get user by email
    // Create user
    // Update user
    // Delete user



}
