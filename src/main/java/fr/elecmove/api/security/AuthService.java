package fr.elecmove.api.security;

import fr.elecmove.api.controller.dto.LoginCredentialsDTO;
import fr.elecmove.api.controller.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;


public interface AuthService {

    LoginResponseDTO login(LoginCredentialsDTO credentials);

    String generateRefreshToken(String idUser);

    TokenPair validateRefreshToken(String token);

}
