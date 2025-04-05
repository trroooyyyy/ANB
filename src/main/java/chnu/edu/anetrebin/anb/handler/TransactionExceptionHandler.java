package chnu.edu.anetrebin.anb.handler;

import chnu.edu.anetrebin.anb.exceptions.transaction.TransactionException;
import chnu.edu.anetrebin.anb.exceptions.transaction.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static chnu.edu.anetrebin.anb.handler.ResponseBuilder.constructResponseEntity;

@RestControllerAdvice
public class TransactionExceptionHandler {
    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handle(TransactionNotFoundException ex) {
        return constructResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handle(TransactionException ex) {
        return constructResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }
}
