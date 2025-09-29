package fr.elecmove.api.security;

import fr.elecmove.api.controller.dto.LoginCredentialsDTO;
import fr.elecmove.api.controller.dto.LoginResponseDTO;
import fr.elecmove.api.controller.dto.mapper.UserMapper;
import fr.elecmove.api.controller.dto.user.UserConnectedDTO;
import fr.elecmove.api.model.RefreshToken;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.RefreshTokenRepository;
import fr.elecmove.api.repository.UserRepository;
import fr.elecmove.api.security.exception.AccountNotValidatedException;
import fr.elecmove.api.security.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;



    public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authManager,
                           UserMapper userMapper,
                           RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userMapper = userMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }


    public LoginResponseDTO login(LoginCredentialsDTO credentials) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword())
        );

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User)) {
            throw new RuntimeException("L'objet authentifié n'est pas de type User.");
        }

        User user = (User) principal;

        // Si le compte n’est pas validé
        if (!user.getValidated()) {
            throw new AccountNotValidatedException("Vous devez valider votre compte par mail avant de vous connecter.");
        }

        String token = jwtUtil.generateToken(user);
        UserConnectedDTO userConnectedDTO = userMapper.toConnectedDto(user);

        return new LoginResponseDTO(token, userConnectedDTO);
    }


    public String generateRefreshToken(String userId) {

        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Le compte client n'existe pas")
        );
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getId();
    }


    public TokenPair validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findById(token).orElseThrow();
        if(refreshToken.isExpired()) {
            throw new RuntimeException("Refresh token expired");
        }
        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        String newToken = generateRefreshToken(user.getId());
        String jwt = jwtUtil.generateToken(user);
        return new TokenPair(newToken, jwt);
    }


    @Transactional
    @Scheduled(fixedDelay=24, timeUnit= TimeUnit.HOURS)
    void cleanExpiredToken(){
        refreshTokenRepository.deleteExpired();
    }
}
