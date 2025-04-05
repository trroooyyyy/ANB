package chnu.edu.anetrebin.anb.handler;

import chnu.edu.anetrebin.anb.exceptions.account.AccountCreationException;
import chnu.edu.anetrebin.anb.exceptions.account.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static chnu.edu.anetrebin.anb.handler.ResponseBuilder.constructResponseEntity;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(AccountCreationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleAccountCreationException(AccountCreationException e) {
        return constructResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException e) {
        return constructResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
