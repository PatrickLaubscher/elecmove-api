package fr.elecmove.api.controller;


import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.controller.dto.UpdatePasswordDTO;
import fr.elecmove.api.controller.dto.mapper.UserMapper;
import fr.elecmove.api.controller.dto.user.UserCreationDTO;
import fr.elecmove.api.controller.dto.user.UserResponseDTO;
import fr.elecmove.api.model.User;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountBusiness accountBusiness;
    private final UserMapper userMapper;

    public AccountController(AccountBusiness accountBusiness, UserMapper userMapper) {
        this.accountBusiness = accountBusiness;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserCreationDTO dto) {
        return userMapper.toDto(
                accountBusiness.register(userMapper.toEntity(dto))
        );
    }


    @GetMapping("/validate/{token}")
    public String activate(@PathVariable String token) {
        accountBusiness.activateUser(token);
        return "Account activated, you can now login";
    }

    @PostMapping("/password/{email}")
    public String resetPassword(@PathVariable String email) {
        accountBusiness.resetPassword(email);
        return "Check your email to reset your password";
    }

    @PatchMapping("/password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdatePasswordDTO dto) {
        User user = (User) userDetails;
        accountBusiness.updatePassword(user, dto.getNewPassword());
        return "Password updated";
    }

    @PatchMapping("/delete-account")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        accountBusiness.deleteAccount(user);
        return "Your account has been deleted";

    }

}

