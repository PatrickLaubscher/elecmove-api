package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.AccountBusiness;
import fr.elecmove.api.business.exception.UserAlreadyExistsException;
import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.RefreshTokenRepository;
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
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public AccountBusinessImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil, MailService mailService, RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
        user.setRole("ROLE_USER");
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
        refreshTokenRepository.deleteByUser(userFound);

    }

    @Override
    public User updateUser(String userId, User user) {

        User userFound = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user account doesn't exist"));

        if (user.getFirstname() != null && !user.getFirstname().isEmpty()) {
            userFound.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null && !user.getLastname().isEmpty()) {
            userFound.setLastname(user.getLastname());
        }
        if (user.getMobile() != null) {
            userFound.setMobile(user.getMobile());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userFound.setEmail(user.getEmail());
        }
        userFound.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(userFound);
    }


    @Override
    public void resetPassword(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user account doesn't exist")
        );

        String token = jwtUtil.generateToken(user, Instant.now().plus(1, ChronoUnit.HOURS));
        mailService.sendResetPassword(user, token);

    }

    @Override
    public void deleteAccount(User user) {

        User userFound = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user account doesn't exist")
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user account doesn't exist")
        );

    }



}
