package fr.elecmove.api.controller;


import fr.elecmove.api.business.exception.BusinessException;
import fr.elecmove.api.security.exception.AccountNotValidatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;


@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail userExists(BusinessException e){
        switch(e.getClass().getSimpleName()) {
            case "UserAlreadyExistsException":
                return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Le compte est déjà créé");
            default:
                return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown server problem");
        }
    }

    @ExceptionHandler(AccountNotValidatedException.class)
    public ProblemDetail handleAccountNotValidated(AccountNotValidatedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException e) {
        logger.warn("Tentative de connexion échouée: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Identifiants incorrects"
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        logger.warn("Erreur d'authentification: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        logger.error("Erreur serveur non gérée: {}", e.getClass().getSimpleName(), e);

        String detail = e.getMessage() != null ? e.getMessage() : "Une erreur interne est survenue";

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                detail
        );

        problemDetail.setTitle("Erreur serveur");
        problemDetail.setProperty("exception", e.getClass().getSimpleName());

        return problemDetail;
    }

    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handleNullPointerException(NullPointerException e) {
        logger.error("NullPointerException: ", e);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une valeur requise est manquante"
        );
    }

}