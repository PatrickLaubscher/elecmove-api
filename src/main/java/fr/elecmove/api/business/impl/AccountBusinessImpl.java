package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.business.exception.UserAlreadyExistsException;
import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.Role;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.RefreshTokenRepository;
import fr.elecmove.api.repository.RoleRepository;
import fr.elecmove.api.repository.UserRepository;
import fr.elecmove.api.security.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
@Transactional
public class AccountBusinessImpl implements AccountBusiness {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public AccountBusinessImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil, MailService mailService, RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String rawPwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPwd));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        Role roleUser = roleRepository.findByName("ROLE_USER").get();
        user.setRole(roleUser);
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser, Instant.now().plus(7, ChronoUnit.DAYS));
        mailService.sendEmailValidation(savedUser, token);

        logger.info("New user register :" + user.getEmail());

        return savedUser;

    }


    @Override
    public void activateUser(String token){

        User user = (User)jwtUtil.validateToken(token);
        user.setValidated(true);
        userRepository.save(user);

    }


    @Override
    public void updatePassword(User user, String newPassword) {

        User userFound = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte user n'existe pas")
        );
        String pwd = passwordEncoder.encode(newPassword);
        userFound.setPassword(pwd);
        userFound.setUpdatedAt(LocalDateTime.now());
        userRepository.save(userFound);
        //Optionnel: On invalide tous les refresh token du user (on les supprime en fait)
        //pour le forcer à se reconnecter sur ses devices avec son nouveau mot de passe
        refreshTokenRepository.deleteByUser(userFound);

    }


    @Override
    public void resetPassword(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte user n'existe pas")
        );

        String token = jwtUtil.generateToken(user, Instant.now().plus(1, ChronoUnit.HOURS));
        mailService.sendResetPassword(user, token);

    }

    @Override
    public void deleteAccount(User user) {

        User userFound = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte user n'existe pas")
        );
        refreshTokenRepository.deleteByUser(userFound);

        String anonymous = "anonymous";
        userFound.setFirstname(anonymous);
        userFound.setLastname(anonymous);
        userFound.setEmail(anonymous);
        userFound.setValidated(false);
        userFound.setUpdatedAt(LocalDateTime.now());
        userRepository.save(userFound);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte user n'existe pas")
        );

    }



}
