package controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.User;
import service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    // Initialize the UserService
    private final UserService userService;

    // Constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Get all users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    // Get user by ID
    // Get user by username
    // Get user by email
    // Create user
    // Update user
    // Delete user



}
