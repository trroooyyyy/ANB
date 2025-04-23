package chnu.edu.anetrebin.anb.handler;

import chnu.edu.anetrebin.anb.exceptions.card.CardAlreadyExists;
import chnu.edu.anetrebin.anb.exceptions.card.CardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static chnu.edu.anetrebin.anb.handler.ResponseBuilder.constructResponseEntity;

@RestControllerAdvice
public class CardExceptionHandler {
    @ExceptionHandler(CardAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleCardAlreadyExists(CardAlreadyExists e) {
        return constructResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleCardNotFound(CardNotFoundException e) {
        return constructResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
