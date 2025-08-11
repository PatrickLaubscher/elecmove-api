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
     * @param id
     * @return
     */
    UserAddress getUserAddress(String id);


    /**
     *
     * @param email
     * @return
     */
    List<UserAddress> getAllUserAddressByEmail(String email);


    /**
     *
     * @param id
     * @param userAddress
     * @param user
     * @return
     */
    UserAddress updateUserAddress(String id, UserAddress userAddress, User user);


    /**
     *
     * @param id
     * @return
     */
    void deleteUserAddress(String id, User user);

}
