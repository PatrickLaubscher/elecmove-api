package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.business.exception.UserAlreadyExistsException;
import fr.elecmove.api.model.Role;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.RoleRepository;
import fr.elecmove.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AccountBusinessImpl implements AccountBusiness {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AccountBusinessImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String rawPwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPwd));

        Role roleUser = roleRepository.findByName("ROLE_USER").get();
        user.setRole(roleUser);

        User savedUser = userRepository.save(user);

        return savedUser;

    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte user n'existe pas")
        );

    }

    @Override
    public void resetPassword(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }

    @Override
    public void deleteAccount(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }




}
