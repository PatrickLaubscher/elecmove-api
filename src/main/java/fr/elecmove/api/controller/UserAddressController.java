package fr.elecmove.api.controller;


import fr.elecmove.api.business.UserAddressBusiness;
import fr.elecmove.api.controller.dto.mapper.UserAddressMapper;
import fr.elecmove.api.controller.dto.user_address.UserAddressCreationDTO;
import fr.elecmove.api.controller.dto.user_address.UserAddressDTO;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.UserAddress;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user-addresses")
public class UserAddressController {

    public UserAddressController(UserAddressBusiness userAddressBusiness, UserAddressMapper userAddressMapper) {
        this.userAddressBusiness = userAddressBusiness;
        this.userAddressMapper = userAddressMapper;
    }

    private final UserAddressBusiness userAddressBusiness;
    private final UserAddressMapper userAddressMapper;



    @GetMapping
    public List<UserAddressDTO> getUserAddresses(@AuthenticationPrincipal UserDetails user) {
        List<UserAddressDTO> userAddressDTOS = new ArrayList<>();
        for(UserAddress userAddress : userAddressBusiness.getUserAddressByEmail(user.getUsername())){
            userAddressDTOS.add(userAddressMapper.toDto(userAddress));
        }
        return userAddressDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAddressDTO createUserAddress(@RequestBody @Valid UserAddressCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return userAddressMapper.toDto(
                userAddressBusiness.createUserAddress(userAddressMapper.toEntity(dto), user)
        );
    }



}
