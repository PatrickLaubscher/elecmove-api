package fr.elecmove.api.controller;

import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.controller.dto.mapper.UserMapper;
import fr.elecmove.api.controller.dto.user.UserCreationDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {


    private final AccountBusiness accountBusiness;
    private final UserMapper userMapper;

    public UserController(AccountBusiness accountBusiness, UserMapper userMapper) {
        this.accountBusiness = accountBusiness;
        this.userMapper = userMapper;
    }


    //@GetMapping
    /*public UserListDTO getAllUsers() {
        return userMapper.toUserListDTO(userService.getAllUsers());
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Valid UserCreationDTO userDto) {
        return userMapper.toDto(
                accountBusiness.register(userMapper.toEntity(userDto))
        );
    }




}
