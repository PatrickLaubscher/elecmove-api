package fr.elecmove.api.controller;

import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.controller.dto.mapper.UserMapper;
import fr.elecmove.api.controller.dto.user.UserPatchDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import fr.elecmove.api.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final AccountBusiness accountBusiness;
    private final UserMapper userMapper;

    public UserController(AccountBusiness accountBusiness, UserMapper userMapper) {
        this.accountBusiness = accountBusiness;
        this.userMapper = userMapper;
    }


    @GetMapping("/me")
    public UserResponseDTO getUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return userMapper.toDto(accountBusiness.findUserByEmail(user.getEmail()));
    }


    @PatchMapping("/me")
    public UserResponseDTO updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserPatchDTO dto) {
        User user = (User) userDetails;
        return userMapper.toDto(
                accountBusiness.updateUser(user.getId(), userMapper.toPatchEntity(dto))
        );
    }


}
