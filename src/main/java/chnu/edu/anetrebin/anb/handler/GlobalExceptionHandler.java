package chnu.edu.anetrebin.anb.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static chnu.edu.anetrebin.anb.handler.ResponseBuilder.constructResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Invalid request format.";

        if (ex.getMessage() != null && ex.getMessage().contains("java.time.LocalDate")) {
            message = "Invalid date format. Date must be in the format yyyy-MM-dd";
        }

        if (ex.getMessage() != null && ex.getMessage().contains("JSON parse error")) { // Don't flexibly, change
            message = "Invalid currency format. Currency must be EUR, UAH or USD";
        }

        return constructResponseEntity(HttpStatus.BAD_REQUEST, message);
    }
}
