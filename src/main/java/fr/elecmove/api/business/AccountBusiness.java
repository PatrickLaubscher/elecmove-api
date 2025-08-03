package fr.elecmove.api.business;


import fr.elecmove.api.model.User;

public interface AccountBusiness {
    User register(User user);
    User findUserByEmail(String email);
    void resetPassword(String email);
    void deleteAccount(User user);
}
