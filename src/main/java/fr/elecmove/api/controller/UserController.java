package fr.elecmove.api.controller;

import java.util.List;

import fr.elecmove.api.dto.RoleDTO;
import fr.elecmove.api.dto.UserCreationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.elecmove.api.dto.UserResponseDTO;
import fr.elecmove.api.service.UserServiceImpl;



@RestController
@RequestMapping("/users")
public class UserController {

    // Initialize the UserService
    private final UserServiceImpl userService;

    // Constructor
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // Get list of all users
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> {
                    UserResponseDTO userDTO = new UserResponseDTO();
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
                    userDTO.setRole(new RoleDTO(user.getRole().getName()));
                    return userDTO;
                })
                .toList();
    }

    // Create user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreationDTO userDto) {
        UserResponseDTO createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    // Get user by ID
    // Get user by username
    // Get user by email
    // Update user
    // Delete user



}
