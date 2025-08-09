package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.UserAddressBusiness;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.UserAddress;
import fr.elecmove.api.repository.UserAddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
public class UserAddressBusinessImpl implements UserAddressBusiness {

    private final UserAddressRepository userAddressRepository;

    public UserAddressBusinessImpl(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }


    @Override
    public UserAddress createUserAddress(UserAddress userAddress, User user) {
        userAddress.setUser(user);
        return userAddressRepository.save(userAddress);
    }


    @Override
    public List<UserAddress> getUserAddressByEmail(String email) {
        return userAddressRepository.findUserAddressByUserEmail(email);
    }


    @Override
    public UserAddress updateUserAddress(UserAddress userAddress, User user) {

        UserAddress existingAddress = userAddressRepository.findById(userAddress.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The address does not exist")
        );
        if(!existingAddress.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify this address.");
        }

        return userAddressRepository.save(userAddress);
    }


    @Override
    public void deleteUserAddress(UserAddress userAddress, User user) {

        UserAddress existingAddress = userAddressRepository.findById(userAddress.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The address does not exist")
        );

        if(!existingAddress.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this address.");
        }

        userAddressRepository.delete(existingAddress);
    }


}
