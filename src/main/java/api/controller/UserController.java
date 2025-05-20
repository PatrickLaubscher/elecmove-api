package api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.model.User;
import api.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    // Initialize the UserService
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Constructor
    public UserController(UserService userService) {
        logger.info("getAllUsers() called"); 
        this.userService = userService;
    }

    @GetMapping
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
