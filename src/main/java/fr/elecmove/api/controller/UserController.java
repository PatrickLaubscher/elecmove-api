package fr.elecmove.api.controller;

import java.util.List;

import fr.elecmove.api.controller.dto.mapper.UserMapper;
import fr.elecmove.api.controller.dto.user.UserCreationDTO;
import fr.elecmove.api.controller.dto.user.UserListDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import fr.elecmove.api.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    public UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public UserListDTO getAllUsers() {
        return userMapper.toUserListDTO(userService.getAllUsers());
    }

    // Create user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreationDTO userDto) {
        UserResponseDTO createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }




}
