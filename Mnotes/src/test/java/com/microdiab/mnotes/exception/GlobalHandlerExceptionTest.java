package com.microdiab.mnotes.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalHandlerExceptionTest {

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @InjectMocks
    private GlobalHandlerException globalHandlerException;

    @Test
    void handleValidationExceptions_ShouldReturnMapOfFieldErrors() {
        // Arrange
        String fieldName = "testField";
        String errorMessage = "must not be null";
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn(fieldName);
        when(fieldError.getDefaultMessage()).thenReturn(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = globalHandlerException.handleValidationExceptions(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(errorMessage, response.getBody().get(fieldName));
    }
}
