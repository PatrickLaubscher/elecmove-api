package fr.elecmove.api.controller;


import fr.elecmove.api.controller.dto.LoginCredentialsDTO;
import fr.elecmove.api.controller.dto.LoginResponseDTO;

import fr.elecmove.api.security.AuthService;
import fr.elecmove.api.security.TokenPair;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("")
public class AuthController {

    private final AuthService authservice;


    public AuthController(AuthService authservice) {
        this.authservice = authservice;
    }

    public ResponseCookie generateCookie(String refreshToken) {

        return ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(false) // change to true when we will in https
                .sameSite("Lax")
                .path("/")
                .build()
                ;
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginCredentialsDTO credentials) {
        LoginResponseDTO responseDTO = authservice.login(credentials);
        String refreshToken = authservice.generateRefreshToken(responseDTO.getUser().getId());
        ResponseCookie refreshCookie = generateCookie(refreshToken);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseDTO);
    }

    @PostMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(name="refresh-token") String token) {

        try {
            TokenPair tokens = authservice.validateRefreshToken(token);
            ResponseCookie refreshCookie = generateCookie(tokens.getRefreshToken());
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(tokens.getJwt());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token");
        }

    }

}