package com.microdiab.mnotes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * This class centralizes the handling of exceptions across the application,
 * providing consistent error responses for validation errors and custom exceptions.
 *
 * It uses Spring's {@link RestControllerAdvice} to apply exception handling globally.
 *
 * Supported exceptions include:
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} for validation errors</li>
 * </ul>
 *
 * @see MethodArgumentNotValidException
 * @see RestControllerAdvice
 * @see ExceptionHandler
 */
@RestControllerAdvice
public class GlobalHandlerException {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerException.class);


    /**
     * Handles validation exceptions thrown when method arguments fail validation.
     *
     * @param ex The validation exception containing the errors.
     * @return A {@link ResponseEntity} with a map of field names to error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
