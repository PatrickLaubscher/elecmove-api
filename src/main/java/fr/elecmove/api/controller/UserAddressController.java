package fr.elecmove.api.controller;


import fr.elecmove.api.business.UserAddressBusiness;
import fr.elecmove.api.controller.dto.mapper.UserAddressMapper;
import fr.elecmove.api.controller.dto.user_address.UserAddressCreationDTO;
import fr.elecmove.api.controller.dto.user_address.UserAddressDTO;
import fr.elecmove.api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-addresses")
public class UserAddressController {

    public UserAddressController(UserAddressBusiness userAddressBusiness, UserAddressMapper userAddressMapper) {
        this.userAddressBusiness = userAddressBusiness;
        this.userAddressMapper = userAddressMapper;
    }

    private final UserAddressBusiness userAddressBusiness;
    private final UserAddressMapper userAddressMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAddressDTO createUserAddress(@RequestBody @Valid UserAddressCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return userAddressMapper.toDto(
                userAddressBusiness.createUserAddress(userAddressMapper.toEntity(dto), user)
        );
    }



}
