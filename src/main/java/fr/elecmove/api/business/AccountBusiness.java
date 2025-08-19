package fr.elecmove.api.business;


import fr.elecmove.api.model.User;

public interface AccountBusiness {

    /**
     *
     * @param user
     * @return User
     */
    User register(User user);

    /**
     *
     * @param token
     */
    void activateUser(String token);


    /**
     *
     * @param email
     * @return
     */
    User findUserByEmail(String email);


    /**
     *
     * @param email
     */
    void resetPassword(String email);

    /**
     *
     * @param user
     * @param newPassword
     */
    void updatePassword(User user, String newPassword);


    /**
     *
     * @param user
     */
    User updateUser(String userId, User user);


    /**
     *
     * @param user
     */
    void deleteAccount(User user);
}
