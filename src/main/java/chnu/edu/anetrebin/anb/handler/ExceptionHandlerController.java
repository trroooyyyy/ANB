package chnu.edu.anetrebin.anb.handler;

import chnu.edu.anetrebin.anb.dto.responses.ExceptionResponse;
import chnu.edu.anetrebin.anb.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {
    private ResponseEntity<Object> constructResponseEntity(HttpStatus status, String message) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                message
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handle(UserNotFoundException ex) {
        return constructResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExists.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handle(UserAlreadyExists ex) {
        return constructResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Invalid request format.";

        if (ex.getMessage() != null && ex.getMessage().contains("java.time.LocalDate")) {
            message = "Invalid date format. Date must be in the format yyyy-MM-dd.";
        }

        return constructResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
