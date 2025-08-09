package fr.elecmove.api.business;

import fr.elecmove.api.model.User;
import fr.elecmove.api.model.UserAddress;

import java.util.List;

public interface UserAddressBusiness {


    /**
     *
     * @param userAddress
     * @param user
     * @return
     */
    UserAddress createUserAddress(UserAddress userAddress, User user);


    /**
     *
     * @param email
     * @return
     */
    List<UserAddress> getUserAddressByEmail(String email);


    /**
     *
     * @param userAddress
     * @param user
     * @return
     */
    UserAddress updateUserAddress(UserAddress userAddress, User user);


    /**
     *
     * @param userAddress
     * @return
     */
    void deleteUserAddress(UserAddress userAddress, User user);

}
