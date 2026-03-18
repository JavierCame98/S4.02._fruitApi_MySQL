package cat.itacademy.s04.t02.n02.fruit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDetails> handleValidationErrors (MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ValidationErrorsDetails details = new ValidationErrorsDetails(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation failed", errors
        );

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAlreadyExist(EntityAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDetails> handleIllegalState(IllegalStateException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralError(Exception ex) {
        return buildResponse("An unexpected internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<ErrorDetails> buildResponse(String message, HttpStatus status) {
        ErrorDetails details = new ErrorDetails(LocalDateTime.now(), status.value(), message);
        return new ResponseEntity<>(details, status);
    }

    public record ErrorDetails(LocalDateTime timestamp, int status, String message) {}
    public record ValidationErrorsDetails (LocalDateTime time, int status, String message, Map<String, String> errors){}
}
