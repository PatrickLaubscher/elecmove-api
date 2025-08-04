package fr.elecmove.api.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.elecmove.api.security.UserService;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtUtil {

    private UserService userService;
    private KeyManager keyManager;

    public JwtUtil(UserService userService, KeyManager keyManager) {
        this.userService = userService;
        this.keyManager = keyManager;
    }


    public String generateToken(UserDetails user) {
        return generateToken(user, Instant.now().plus(30, ChronoUnit.MINUTES));
    }



    public String generateToken(UserDetails user, Instant expiration) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiration)
                .sign(keyManager.getAlgorithm());
    }


    public UserDetails validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT
                    .require(keyManager.getAlgorithm())
                    .build()
                    .verify(token);
            String userIdentifier = decodedJWT.getSubject();
            return userService.loadUserByUsername(userIdentifier);
        } catch (JWTVerificationException | UsernameNotFoundException e) {
            throw new AuthorizationDeniedException("Error verifying token");
        }
    }

}
