package fr.elecmove.api.controller;


import fr.elecmove.api.business.exception.BusinessException;
import fr.elecmove.api.security.exception.AccountNotValidatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {

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

}