package api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.UserDTO;
import api.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    // Initialize the UserService
    private final UserService userService;

    // Constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setFirstname(user.getFirstname());
                    userDTO.setLastname(user.getLastname());
                    userDTO.setBirthdate(user.getBirthdate().toString());
                    userDTO.setMobile(user.getMobile());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setAddress(user.getAddress());
                    userDTO.setZipcode(user.getZipcode());
                    userDTO.setCity(user.getCity());
                    userDTO.setIsValidated(user.getIsValidated());
                    userDTO.setCreatedAt(user.getCreatedAt().toString());
                    userDTO.setUpdatedAt(user.getUpdatedAt().toString());
                    userDTO.setRoleId(user.getRoleId());
                    return userDTO;
                })
                .toList();
    }


    // Get user by ID
    // Get user by username
    // Get user by email
    // Create user
    // Update user
    // Delete user



}
