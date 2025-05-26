package api.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.service.JWTService;


@RestController
@RequestMapping("/auth")
public class LoginController {


    private final JWTService jwtService;
    
    public LoginController(JWTService jwtService) {
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    public String getToken(Authentication authentication) {
                String token = jwtService.generateToken(authentication);
                return token;
    }
    
}