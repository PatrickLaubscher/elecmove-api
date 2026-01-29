package fr.elecmove.api.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountNotValidatedException extends RuntimeException {
    public AccountNotValidatedException(String message) {
        super(message);
    }
}